package com.shadrin.stockmarket.cli.actions;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Date;

public class AddAction extends Action {
    private String symbol;
    private OrderType orderType;
    private int count;
    private int price;

    @Override
    public void run() {
        String json = String.format(
                "{\"symbol\":\"%s\", \"type\":\"%s\", \"count\":%d, \"price\":%d}",
                symbol,
                orderType,
                count,
                price
        );
        var requestBody = RequestBody.create(
                MediaType.parse("application/json"), json);
        var request = new Request.Builder().url(url + "/order/add")
                        .post(requestBody).build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            if (response.isSuccessful()) {
                System.out.printf("[%s] %s is created%n", new Date(System.currentTimeMillis()), response.body().string());
            } else {
                System.err.println("Error adding a new order. Server returned response code: " + response.code());
            }
        } catch (IOException e) {
            System.err.println("Error adding a new order. " + e.getMessage());
        }
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public enum OrderType {
        BUY,
        SELL
    }
}
