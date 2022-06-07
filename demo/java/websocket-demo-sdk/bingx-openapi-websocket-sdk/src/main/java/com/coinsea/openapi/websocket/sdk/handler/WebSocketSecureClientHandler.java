package com.coinsea.openapi.websocket.sdk.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import static io.netty.handler.timeout.IdleState.WRITER_IDLE;

/**
 * @author admin
 */
public class WebSocketSecureClientHandler extends SimpleChannelInboundHandler<Object> {

    private final static Logger logger = LoggerFactory.getLogger(WebSocketSecureClientHandler.class);


    private WebSocketClientHandshaker handshaker;
    private OnOpenHandler onOpenHandler;
    private OnMessageHandler onMessageHandler;
    private OnCloseHandler onCloseHandler;
    private URI uri;
    private Map<String, String> attachingHeaders;
    private ChannelPromise handshakeFuture;


    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        System.out.println("handlerAdded, handshakeFuture add");
        handshakeFuture = ctx.newPromise();
    }

    public WebSocketSecureClientHandler(OnOpenHandler onOpenHandler, OnMessageHandler onMessageHandler, OnCloseHandler onCloseHandler,
                                        URI uri, Map<String, String> headers) {
        this.onOpenHandler = onOpenHandler;
        this.onCloseHandler = onCloseHandler;
        this.onMessageHandler = onMessageHandler;
        this.uri = uri;
        this.attachingHeaders = headers;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive");
        DefaultHttpHeaders headers = new DefaultHttpHeaders();
        headers.add(HttpHeaderNames.HOST, uri.getHost());
        if (attachingHeaders != null) {
            attachingHeaders.forEach(headers::add);
        }
        handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                uri, WebSocketVersion.V13, null, true, headers, 209715200);
        handshaker.handshake(ctx.channel());
        logger.info("handshake init");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        if (!handshaker.isHandshakeComplete()) {
            handshaker.finishHandshake(channelHandlerContext.channel(), (FullHttpResponse) msg);
            onOpenHandler.onOpen(channelHandlerContext.channel());
            handshakeFuture.setSuccess();
            logger.info("handshake finish");
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            logger.info("Unexpected FullHttpResponse:{}", response.content().toString(CharsetUtil.UTF_8));
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.status() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            onMessageHandler.onMessage(channelHandlerContext.channel(), textFrame.text());
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) frame;
            ByteBuf content = binaryWebSocketFrame.content();
            byte[] bytes = new byte[content.capacity()];
            content.readBytes(bytes, 0, content.capacity());
            String rec = new String(uncompress(bytes), StandardCharsets.UTF_8);
            onMessageHandler.onMessage(channelHandlerContext.channel(), rec);
        } else if (frame instanceof PingWebSocketFrame){
            logger.info("channel read PingWebSocketFrame");
        } else if (frame instanceof PongWebSocketFrame) {
        } else if (frame instanceof CloseWebSocketFrame) {
            logger.info("channel read CloseWebSocketFrame, remoteAddress:{}", channelHandlerContext.channel().remoteAddress());
            channelHandlerContext.channel().close();
        }
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        } catch (IOException e) {
            logger.error("gzip uncompress error.", e);
        }

        return out.toByteArray();
    }

    public String convertByteBufToString(ByteBuf buf) {
        String str;
        if (buf.hasArray()) { // 处理堆缓冲区
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes(), StandardCharsets.UTF_8);
        } else { // 处理直接缓冲区以及复合缓冲区
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, StandardCharsets.UTF_8);
        }
        return str;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channel close, remoteAddress:{}", ctx.channel().remoteAddress());
        onCloseHandler.onClose(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
        logger.warn("channel error", t);
    }

    @FunctionalInterface
    public interface OnMessageHandler {
        void onMessage(Channel channel, String text);
    }

    @FunctionalInterface
    public interface OnCloseHandler {
        void onClose(Channel channel);
    }

    @FunctionalInterface
    public interface OnOpenHandler {
        void onOpen(Channel channel);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (WRITER_IDLE.equals(idleStateEvent.state())) {
                ctx.channel().writeAndFlush(new PingWebSocketFrame());
            }
        }
    }
}
