package com.pedidosya.test.bootstrap;

import lombok.extern.log4j.Log4j;
import spark.Spark;

@Log4j
public class Main {
    public static void main(String[] args) {
        Spark.port(8080);
        new Router().init();
        log.info("Listening on http://localhost:8080/");
    }
}
