package com.shadrin.stockmarket.cli.actions;

import okhttp3.OkHttpClient;

public abstract class Action {
    protected OkHttpClient okHttpClient;
    protected String url;

    public void setClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public abstract void run();
}
