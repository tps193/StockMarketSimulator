package com.shadrin.stockmarket.cli.actions;

import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Date;

public class CancelAction extends Action {
    private final long orderId;

    public CancelAction(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public void run() {
        var request = new Request.Builder()
                .url(url + "/order/cancel/" + orderId)
                .post(RequestBody.create(new byte[0]))
                .build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            if (response.isSuccessful()) {
                System.out.printf("[%s] Order %d is cancelled%n", new Date(System.currentTimeMillis()), orderId);
            } else {
                System.out.printf("Error cancelling order %d. Server response %s %n", orderId, response);
            }
        } catch (IOException e) {
            System.err.printf("Error cancelling order %d. %s%n", orderId, e.getMessage());
        }
    }
}
