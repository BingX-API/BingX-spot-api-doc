package com.coinsea.openapi.websocket.sdk;

import com.coinsea.openapi.websocket.sdk.constant.HandleType;
import com.coinsea.openapi.websocket.sdk.handler.WorkerHandler;
import com.coinsea.openapi.websocket.sdk.annotation.MsgReceivedListener;
import com.coinsea.openapi.websocket.sdk.handler.WebSocketSecureClientHandler;
import com.coinsea.openapi.websocket.sdk.pojo.Message;
import com.coinsea.openapi.websocket.sdk.pojo.Subscribe;
import com.coinsea.openapi.websocket.sdk.util.JacksonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.IdleStateHandler;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Till
 */
public class WebSocketSecureClient {
    private final static Logger logger = LoggerFactory.getLogger(WebSocketSecureClient.class);

    private String path = "/market";

    private Integer port = 443;

    private String host = "open-api-ws.bingx.com";

    private String protocol = "wss";

    private SslContext sslCtx;

    private Channel channel;

    private final EventLoopGroup group = new NioEventLoopGroup(1);

    private final Map<Integer, List<WorkerHandler>> workerMap = new HashMap<>();

    private Executor executorService = Executors.newSingleThreadExecutor();

    private WebSocketSecureClientHandler clientHandler;

    private void initClient() {
        Bootstrap bootstrap = new Bootstrap();
        if ("wss".equals(protocol)) {
            try {
                sslCtx = SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } catch (SSLException e) {
                logger.error("SslContext init failed", e);
            }
        }

        bootstrap.group(group)
                .option(ChannelOption.TCP_NODELAY, true)
                //.option(ChannelOption.SO_BACKLOG, 100000)
                .channel(NioSocketChannel.class);
        String url;
        if (sslCtx == null) {
            if (port < 1) {
                port = 80;
            }
            url = "ws://" + host + ":" + port;
        } else {
            if (port < 1) {
                port = 443;
            }
            url = "wss://" + host + ":" + port;
        }
        if (path != null && !"".equals(path)) {
            url += path;
        }

        URI uri = URI.create(url);

        logger.info("host:{}, port:{}, uri:{}, {}, {}, {}", host, port, uri.toString(), uri.getHost(), uri.getPort(), uri.getPath());
        Map<String, String> headers = new HashMap<>(8);
        String finalHost = host;
        String finalUrl = url;
        clientHandler = new WebSocketSecureClientHandler(new OnOpenHandlerImpl(),
                new OnMessageHandlerImpl(), new OnCloseHandlerImpl(), uri, headers);

        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        if (sslCtx != null) {
                            SslHandler sslHandler = sslCtx.newHandler(ch.alloc(), uri.getHost(), port);
                            pipeline.addLast(sslHandler);
                        }
                        pipeline.addLast(
                                new HttpClientCodec(),
                                new HttpObjectAggregator(209715200), WebSocketClientCompressionHandler.INSTANCE,
                                new IdleStateHandler(0, 3, 0),
                                clientHandler);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.connect(host, port);
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    logger.info("connect success");
                }
            });
            channel = channelFuture.channel();
        } catch (Exception e) {
            logger.warn("link server error, host:{} port:{}", url, port, e);
        }
    }

    public <T> void subscribe(Subscribe subscribe) {
        String msgJson;
        try {
            msgJson = JacksonUtils.obj2json(subscribe);
        } catch (JsonProcessingException e) {
            logger.error("msg serialize fail", e);
            throw new RuntimeException("msg serialize fail");
        }
        logger.info("subscribe :{}", msgJson);

        if (!clientHandler.handshakeFuture().isSuccess()) {
            // send msg, must handshake  before
            try {
                clientHandler.handshakeFuture().sync();
            } catch (Exception e) {
                throw new RuntimeException("fail");
            }
        }
        ChannelFuture channelFuture = channel.writeAndFlush(new TextWebSocketFrame(msgJson));
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("subscribe success");
            }
        });
    }

    private void scanMsgListener() {
        Reflections reflections = new Reflections(new MethodAnnotationsScanner());
        Set<Method> resources = reflections.getMethodsAnnotatedWith(MsgReceivedListener.class);
        logger.info("MsgReceivedListener method size:{}", resources.size());
        if (resources != null && resources.size() > 0) {
            for (Method resource : resources) {
                Object o;
                // TODO it may be wrong in spring environment
                try {
                     o = resource.getDeclaringClass().newInstance();
                }catch (Exception e){
                    logger.error("class instantiated fail", e);
                    throw new RuntimeException("class instantiated fail");
                }
                MsgReceivedListener annotation = resource.getAnnotation(MsgReceivedListener.class);
                int type = annotation.type();
                resource.setAccessible(true);
                WorkerHandler workerHandler = new WorkerHandler(o, resource);
                List<WorkerHandler> handlers = workerMap.containsKey(type) ? workerMap.get(type) : new ArrayList<>();
                handlers.add(workerHandler);
                workerMap.put(type, handlers);
            }
        }
    }


    private class OnMessageHandlerImpl implements WebSocketSecureClientHandler.OnMessageHandler {
        @Override
        public void onMessage(Channel channel, String text) {
            if (!channel.isActive()) {
                logger.warn("onMessage is inActive， remoteAddress:{}  ", channel.remoteAddress());
                return;
            }
            Message<?> message = null;
            try {
                message = JacksonUtils.json2pojo(text, Message.class);
            } catch (Exception e) {
                logger.error("can not phrase text:{}", text);
                return;
            }

            List<WorkerHandler> handlers = workerMap.get(HandleType.MARKET);

            if (handlers == null || handlers.size() == 0) {
                logger.warn("not found worker");
                return;
            }

            for (WorkerHandler handler : handlers) {
                // run in single thread
                Message<?> finalMessage = message;
                executorService.execute(() -> {
                    handler.exec(finalMessage);
                });
            }
        }
    }

    private void OnCloseHandler(Channel channel) {

    }

    private class OnCloseHandlerImpl implements WebSocketSecureClientHandler.OnCloseHandler {
        @Override
        public void onClose(Channel channel) {
            logger.info("channel onClose");
        }
    }

    private class OnOpenHandlerImpl implements WebSocketSecureClientHandler.OnOpenHandler {
        @Override
        public void onOpen(Channel channel) {
            logger.info("channel onOpen");
        }
    }

    public WebSocketSecureClient(String protocol, String host, Integer port, String path) {
        this.host = host;
        this.protocol = protocol;
        this.port = port;
        this.path = path;
        scanMsgListener();
        initClient();
    }

}
