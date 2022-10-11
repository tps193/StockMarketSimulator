package com.shadrin.stockmarket.cli;

import com.shadrin.stockmarket.cli.actions.ActionCreator;
import com.shadrin.stockmarket.cli.websocket.AppWebSocketListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class App {
    public static void main(String...args) {
        URL url = null;
        switch(args.length) {
            case 0 -> {
                exitWithError("Missing Stock Market Simulator URL.");
            }
            case 1 -> {
                try {
                    url = new URL(args[0]);
                } catch (MalformedURLException e) {
                    exitWithError("Invalid URL format - " + e.getMessage());
                }
            }
            default -> {
                exitWithError("Too many command line options.");
            }
        }
        printHelp();

        OkHttpClient okHttpClient = new OkHttpClient();
        pingServer(okHttpClient, url);
        connectWebSocket(okHttpClient, url);

        try (var is = new InputStreamReader(System.in); var reader = new BufferedReader(is)) {
            var actionCreator = new ActionCreator(url.toString(), okHttpClient);
            while(true) {
                if(reader.ready()) {
                    var userInput = reader.readLine();
                    try {
                        var action = actionCreator.getAction(userInput);
                        action.run();
                    } catch (CommandLineParseException e) {
                        System.err.println(e.getMessage());
                    }
                }

            }
        } catch (IOException e) {
            System.err.println("Error reading user input. " + e.getMessage());
            System.exit(1);
        }
    }

    private static void printHelp() {
        System.out.println("Welcome to Stock Market Simulator Client");
        System.out.println("Available actions: " + Arrays.toString(ActionCreator.actions));
        System.out.println("Usage example:");
        System.out.println("add APPL BUY 10 50");
        System.out.println("cancel 10");
        System.out.println("addSymbol APPL");
        System.out.println("quit");

    }

    private static void connectWebSocket(OkHttpClient okHttpClient, URL url) {
        var websocketUrl = String.format("ws://%s:%d", url.getHost(), url.getPort());
        var socketRequest = new Request.Builder()
                .url(websocketUrl + "/trades-ws")
                .build();
        okHttpClient.newWebSocket(socketRequest, new AppWebSocketListener());
    }

    private static void pingServer(OkHttpClient okHttpClient, URL url) {
        var request = new Request.Builder().url(url + "/ping").build();
        var call = okHttpClient.newCall(request);
        try {
            var response = call.execute();
            if (!response.isSuccessful()) {
                exitWithError(String.format("Can't connect to Stock Market Simulator. Error code %d", response.code()));
            }
            System.out.println(url + " is up");
        } catch (IOException e) {
            exitWithError(String.format("Can't connect to Stock Market Simulator. Exception: %s", e.getMessage()));
        }
    }

    public static void exitWithError(String message) {
        System.err.println(message);
        System.err.println("Usage: java -jar stock-market-cli.jar <URL>");
        System.exit(1);
    }
}
