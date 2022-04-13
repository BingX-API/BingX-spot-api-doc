package main

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"time"
)

const (
	urlStr    = "https://open-api.bingx.com"
	apiKey    = "Set your api key here!!"
	secretKey = "Set your secret key here!!"
)

func computeHmac256(strMessage string, strSecret string) string {
	key := []byte(strSecret)
	h := hmac.New(sha256.New, key)
	h.Write([]byte(strMessage))

	return hex.EncodeToString(h.Sum(nil))
}

func post(requestUrl string) (responseData []byte, err error) {
	u, _ := url.Parse(requestUrl)
	req := http.Request{
		Method: http.MethodPost,
		URL:    u,
		Header: http.Header{},
	}
	req.Header.Set("X-BX-APIKEY", apiKey)
	cli := http.Client{
		Timeout: 3 * time.Second,
	}
	response, err := cli.Do(&req)
	if err != nil {

		return
	}
	defer response.Body.Close()

	responseData, err = ioutil.ReadAll(response.Body)
	return
}

func placeOrder(symbol, side, tradeType, quantity, quoteOrderQty, price string) {
	requestPath := "/openApi/spot/v1/trade/order"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("side", side)
	mapParams.Set("type", tradeType)
	mapParams.Set("quantity", quantity)
	mapParams.Set("quoteOrderQty", quoteOrderQty)
	mapParams.Set("price", price)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := post(requestUrl)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func cancelOrder(symbol, orderID string) {
	requestPath := "/openApi/spot/v1/trade/cancel"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("orderId", orderID)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := post(requestUrl)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func queryOrder(symbol, orderID string) {
	requestPath := "/openApi/spot/v1/trade/query"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("orderId", orderID)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := post(requestUrl)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func queryOpenOrders(symbol string) {
	requestPath := "/openApi/spot/v1/trade/openOrders"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := post(requestUrl)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func queryHistoryOrders(symbol, orderId, startTime, endTime, limit string) {
	requestPath := "/openApi/spot/v1/trade/historyOrders"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("orderId", orderId)
	mapParams.Set("startTime", startTime)
	mapParams.Set("endTime", endTime)
	mapParams.Set("limit", limit)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := post(requestUrl)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func main() {
	//fmt.Println("placeOrder:")
	//placeOrder("XRP-USDT", "BUY", "LIMIT", "20", "0", "0.5")

	//fmt.Println("cancelOrder:")
	//cancelOrder("XRP-USDT", "1513803323449278464")

	//fmt.Println("queryOrder:")
	//queryOrder("XRP-USDT", "1513804323790782464")

	//fmt.Println("queryOpenOrders:")
	//queryOpenOrders("XRP-USDT")

	//fmt.Println("queryHistoryOrders:")
	//queryHistoryOrders("XRP-USDT", "0", "0", "0", "2")
}
