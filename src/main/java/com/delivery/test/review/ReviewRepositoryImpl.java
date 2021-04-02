package com.pedidosya.test.review;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class ReviewRepositoryImpl implements ReviewRepository {

    private List<Review> reviews;

    @Inject
    public ReviewRepositoryImpl() {
        reviews = new ArrayList<>();
    }

    @Override
    public Review getByPurchaseId(String purchaseId) {
        return reviews.stream()
                .filter(review -> purchaseId.equals(review.getPurchaseId()))
                .findFirst().orElse(null);
    }

    @Override
    public void deleteForPurchase(String purchaseId) {
        reviews.removeIf(review -> purchaseId.equals(review.getPurchaseId()));
    }

    @Override
    public Review create(Review review) {
        reviews.add(review);
        return review;
    }

    @Override
    public Review update(Review reviewToModify) {
        reviews.removeIf(review -> review.getPurchaseId().equals(reviewToModify.getPurchaseId()));
        reviews.add(reviewToModify);
        return reviewToModify;
    }

    @Override
    public Collection<Review> search(Map<String, Object> params) {
        if (params.get("date_from") != null && params.get("date_to") != null && params.get("shop_id") != null) {
            String shopId = (String) params.get("shop_id");
            LocalDateTime dateFrom = (LocalDateTime) params.get("date_from");
            LocalDateTime dateTo = (LocalDateTime) params.get("date_to");
            return reviews.stream().filter(review -> shopId.equals(review.getShopId()) && (review.getTimestamp()
                    .isAfter(dateFrom) || review.getTimestamp().isEqual(dateFrom)) &&
                    (review.getTimestamp().isBefore(dateTo) || review.getTimestamp().isEqual(dateTo)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
