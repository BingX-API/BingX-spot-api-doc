import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.TreeMap;

public class TradeDemo {
    String url = "https://open-api.bingx.com";
    String apiKey = "Set your api key here!!";
    String secretKey = "Set your secret key here!!";

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    byte[] hmac(String algorithm, byte[] key, byte[] message) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        return mac.doFinal(message);
    }


    String generateHmac256(String message) {
        try {
            byte[] bytes = hmac("HmacSHA256", secretKey.getBytes(), message.getBytes());
            return bytesToHex(bytes);
        } catch (Exception e) {
            System.out.println("generateHmac256 expection:" + e);
        }
        return "";
    }


    String getMessageToDigest(String method, String path, TreeMap<String, String> parameters) {
        Boolean first = true;
        String valueToDigest = "";
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            if (!first) {
                valueToDigest += "&";
            }
            first = false;
            valueToDigest += e.getKey() + "=" + e.getValue();
        }
        return valueToDigest;
    }

    String getRequestUrl(String path, String parameters) {
        String urlStr = url + path + "?" + parameters;
        return urlStr;
    }

    void execute(String requestUrl, String method) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            http.setRequestMethod(method); // PUT is another valid option
            http.addRequestProperty("X-BX-APIKEY", apiKey);
            http.setDoOutput(true);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            String result = "";
            String line = "";
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            while ((line = in.readLine()) != null) {
                result += line;
            }

            System.out.println("\t" + result);

        } catch (Exception e) {
            System.out.println("expection:" + e);
        }
    }


    void placeOrder(String symbol, String side, String tradeType, String quantity,
                    String quoteOrderQty, String price) {
        String method = "POST";
        String path = "/openApi/spot/v1/trade/order";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("side", side);
        parameters.put("type", tradeType);
        parameters.put("quantity", quantity);
        parameters.put("quoteOrderQty", quoteOrderQty);
        parameters.put("price", price);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        execute(requestUrl, method);
    }

    void cancelOrder(String symbol, String orderId) {
        String method = "POST";
        String path = "/openApi/spot/v1/trade/cancel";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("orderId", orderId);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        execute(requestUrl, method);
    }

    void queryOrder(String symbol, String orderId) {
        String method = "POST";
        String path = "/openApi/spot/v1/trade/query";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("orderId", orderId);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        execute(requestUrl, method);
    }

    void queryOpenOrders(String symbol) {
        String method = "POST";
        String path = "/openApi/spot/v1/trade/openOrders";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        execute(requestUrl, method);
    }

    void queryHistoryOrders(String symbol, String orderId, String startTime,
                            String endTime, String limit) {
        String method = "POST";
        String path = "/openApi/spot/v1/trade/historyOrders";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);

        execute(requestUrl, method);
    }

    void querySymbols(String symbol) {
        String method = "GET";
        String path = "/openApi/spot/v1/common/symbols";
        String timestamp = "" + new Timestamp(System.currentTimeMillis()).getTime();

        TreeMap<String, String> parameters = new TreeMap<String, String>();
        parameters.put("symbol", symbol);
        parameters.put("timestamp", timestamp);

        String valueToDigest = getMessageToDigest(method, path, parameters);
        String messageDigest = generateHmac256(valueToDigest);
        String parametersString = valueToDigest + "&signature=" + messageDigest;
        String requestUrl = getRequestUrl(path, parametersString);
        execute(requestUrl,method);
    }

    public static void main(String[] args) {
        TradeDemo h = new TradeDemo();


//    	System.out.println("placeOrder:");
//    	h.placeOrder("XRP-USDT", "BUY", "LIMIT", "20", "0", "0.5");

//    	System.out.println("cancelOrder:");
//    	h.cancelOrder("XRP-USDT", "1514068719687434240");

//    	System.out.println("queryOrder:");
//    	h.queryOrder("XRP-USDT", "1514068719687434240");

//    	System.out.println("queryOpenOrders:");
//    	h.queryOpenOrders("XRP-USDT");

//    	System.out.println("queryHistoryOrders:");
//    	h.queryHistoryOrders("XRP-USDT", "0", "0", "0", "2");

//    	System.out.println("querySymbols:");
//    	h.querySymbols("BTC-USDT");

    }
}