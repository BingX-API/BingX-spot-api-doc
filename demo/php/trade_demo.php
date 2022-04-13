<?php

$url = "https://open-api.bingx.com";
$apiKey = "Set your api key here!!";
$secretKey = "Set your secret key here!!";

function getOriginString(array $params) {
    // combine origin string
    $originString = "";
    $first = true;
    foreach($params as $n => $v) {
          if (!$first) {
              $originString .= "&";
          }
          $first = false;
          $originString .= $n . "=" . $v;
    }
    return $originString;
}

function getSignature(string $originString) {
    global $secretKey;
    $signature = hash_hmac('sha256', $originString, $secretKey, true);
    $signature = bin2hex($signature);
    return $signature;
}

function getRequestUrl(string $path, array $params) {
    global $url;
    $requestUrl = $url.$path."?";
    $first = true;
    foreach($params as $n => $v) {
          if (!$first) {
              $requestUrl .= "&";
          }
          $first = false;
          $requestUrl .= $n . "=" . $v;
    }
    return $requestUrl;
}

function httpPost($url)
{
    global $apiKey;
    $curl = curl_init($url);
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_USERAGENT, "curl/7.80.0");
    curl_setopt($curl, CURLOPT_HTTPHEADER, array(
        "X-BX-APIKEY:".$apiKey,
    ));
    $response = curl_exec($curl);
    curl_close($curl);
    return $response;
}

function placeOrder(string $symbol, string $side, string $tradeType, string $quantity,
    string $quoteOrderQty, string $price) {

    // interface info
    $path = "/openApi/spot/v1/trade/order";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $params['side'] = $side;
    $params['type'] = $tradeType;
    $params['quantity'] = $quantity;
    $params['quoteOrderQty'] = $quoteOrderQty;
    $params['price'] = $price;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

function cancelOrder(string $symbol, string $orderId) {
    // interface info
    $path = "/openApi/spot/v1/trade/cancel";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $params['orderId'] = $orderId;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

function queryOrder(string $symbol, string $orderId) {
    // interface info
    $path = "/openApi/spot/v1/trade/query";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $params['orderId'] = $orderId;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

function queryOpenOrders(string $symbol) {
    // interface info
    $path = "/openApi/spot/v1/trade/openOrders";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

function queryHistoryOrders(string $symbol, string $orderId, string $startTime,
    string $endTime, string $limit) {
    // interface info
    $path = "/openApi/spot/v1/trade/historyOrders";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $params['orderId'] = $orderId;
    $params['startTime'] = $startTime;
    $params['endTime'] = $endTime;
    $params['limit'] = $limit;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

// echo "placeOrder:\n";
// placeOrder("XRP-USDT", "BUY", "LIMIT", "20", "0", "0.5");

// echo "cancelOrder:\n";
// cancelOrder("XRP-USDT", "1513809777442947072");

// echo "queryOrder:\n";
// queryOrder("XRP-USDT", "1513809777442947072");

// echo "queryOpenOrders:\n";
// queryOpenOrders("XRP-USDT");

// echo "queryHistoryOrders:\n";
// queryHistoryOrders("XRP-USDT", "0", "0", "0", "2");

?>