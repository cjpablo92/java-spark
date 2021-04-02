package com.pedidosya.test.bootstrap;

public enum UrlMappings {
    BASE("/v1/"),
    REVIEWS(String.format("%sreviews", BASE.url)),
    REVIEWS_PURCHASE(String.format("%sreviews/purchases/:purchase_id", BASE.url)),
    REVIEWS_SHOP(String.format("%sreviews/shop/:shop_id", BASE.url));

    public final String url;

    UrlMappings(String url) {
        this.url = url;
    }
}
