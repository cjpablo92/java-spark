package com.pedidosya.test.bootstrap;

import com.google.common.net.MediaType;
import com.google.inject.Injector;
import com.pedidosya.test.review.ReviewController;
import spark.Spark;
import spark.Response;
import spark.servlet.SparkApplication;

import javax.servlet.http.HttpServletResponse;

import static spark.Spark.halt;

public class Router implements SparkApplication {

    @Override
    public void init() {
        Spark.after((request, response) -> setHeaders(response));
        registerSecurityFilter();
        registerExceptionHandler();
        registerPing();
        registerReviewResource();
        // TODO: REGISTER NOT FOUND HANDLER
    }

    private void setHeaders(Response response) {
        if (!response.raw().containsHeader("Content-Type")) {
            response.header("Content-Type", MediaType.JSON_UTF_8.toString());
        }
    }

    private void registerSecurityFilter() {
        Spark.before((request, response) -> {
            boolean authenticated = true;
            //  TODO: check if authenticated
            if (!authenticated) {
                halt(401, "You are not authenticated");
            }
        });
    }

    private void registerExceptionHandler() {
        Spark.exception(ApiException.class, (e, request, response) -> {
            response.status(e.getStatus());
            response.body(e.getJsonMessage());
            setHeaders(response);
        });
    }

    private void registerPing() {
        Spark.get("/ping", (request, response) -> {
            response.status(HttpServletResponse.SC_OK);
            response.header("Content-Type", MediaType.PLAIN_TEXT_UTF_8.toString());
            return "pong";
        });
    }

    private void registerReviewResource() {
        Injector guiceInjector = TestGuiceModule.getInjector();
        ReviewController reviewController = guiceInjector.getInstance(ReviewController.class);
        reviewController.init();
    }
}
