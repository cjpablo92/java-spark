package com.pedidosya.test.review;

import com.pedidosya.test.bootstrap.Response;
import com.pedidosya.test.bootstrap.UrlMappings;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import spark.Spark;
import spark.servlet.SparkApplication;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Collections;

public class ReviewController implements SparkApplication {

    private ReviewService reviewService;
    // TODO: CHECK IF IT COULD BE IN UTILS
    private Gson gson;

    @Inject
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
        this.gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Override
    public void init() {
        Spark.get(UrlMappings.REVIEWS_PURCHASE.url, (request, response) -> {
            response.status(HttpServletResponse.SC_OK);
            if (request.params("purchase_id") != null) {
                Review review = this.reviewService.getByPurchase(request.params("purchase_id"));
                return Response.builder()
                        .data(review)
                        .build().toJson();
            } else {
                response.status(HttpServletResponse.SC_BAD_REQUEST);
                return Response.builder()
                        .data(Collections.singletonMap("Message", "Purchase id is required"))
                        .build().toJson();
            }
        });

        Spark.get(UrlMappings.REVIEWS_SHOP.url, (request, response) -> {
            response.status(HttpServletResponse.SC_OK);
            if (request.params("purchase_id") != null && request.queryParams("date_from") != null
                    && request.queryParams("date_to") != null) {
                Collection<Review> review = this.reviewService.search(request.queryMap().toMap());
                return Response.builder()
                        .data(review)
                        .build().toJson();
            } else {
                response.status(HttpServletResponse.SC_BAD_REQUEST);
                return Response.builder()
                        .data(Collections.singletonMap("Message", "purchase id, date from and date to are required"))
                        .build().toJson();
            }
        });

        Spark.delete(UrlMappings.REVIEWS_PURCHASE.url, (request, response) -> {
            response.status(HttpServletResponse.SC_OK);
            if (request.params("purchase_id") != null) {
                Review review = this.reviewService.deleteForPurchase(request.params("purchase_id"));
                return Response.builder()
                        .data(review)
                        .build().toJson();
            } else {
                response.status(HttpServletResponse.SC_BAD_REQUEST);
                return Response.builder()
                        .data(Collections.singletonMap("Message", "Purchase id is required"))
                        .build().toJson();
            }
        });

        Spark.post(UrlMappings.REVIEWS.url, (request, response) -> {
            response.status(HttpServletResponse.SC_CREATED);
            Review reviewToCreate = gson.fromJson(request.body(), Review.class);

            return Response.builder()
                    .data(this.reviewService.create(reviewToCreate))
                    .build().toJson();

        });
    }
}
