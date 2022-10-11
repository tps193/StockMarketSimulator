package com.shadrin.stockmarket.cli.actions;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Date;

public class AddSymbolAction extends Action {
    private final String symbol;

    public AddSymbolAction(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public void run() {
        var request = new Request.Builder()
                .url(url + "/symbol/add/" + symbol)
                .post(RequestBody.create(new byte[0]))
                .build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            if (response.isSuccessful()) {
                System.out.printf("[%s] Symbol %s is created%n", new Date(System.currentTimeMillis()), symbol);
            } else {
                System.out.printf("Error creating symbol %s. Server response %s %n", symbol, response);
            }
        } catch (IOException e) {
            System.err.printf("Error creating symbol %s. %s%n", symbol, e.getMessage());
        }
    }
}
