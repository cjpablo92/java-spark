package com.pedidosya.test.review;

import com.pedidosya.test.bootstrap.ApiException;
import com.pedidosya.test.bootstrap.AsyncUtils;
import com.google.inject.Inject;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ReviewServiceImpl implements ReviewService {

    private ReviewCacheService reviewCacheService;
    private ReviewRepository reviewRepository;

    @Inject
    public ReviewServiceImpl(ReviewCacheService reviewCacheService, ReviewRepository reviewRepository) {
        this.reviewCacheService = reviewCacheService;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Review get(String reviewId) {
        return null;
    }

    @Override
    public Review getByPurchase(String purchaseId) {
        Review review = this.reviewCacheService.get(purchaseId);
        if (review != null) {
            return review;
        }
        throw new ApiException(HttpServletResponse.SC_NOT_FOUND, String
                .format("Review not found for purchase %s", purchaseId));
    }

    @Override
    public Review create(Review review) {
        review.setTimestamp(LocalDateTime.now());
        review.setId(UUID.randomUUID().toString());
        if (review.getScore() > 5 || review.getScore() < 1) {
            throw new ApiException(HttpServletResponse.SC_BAD_REQUEST, "The review score needs to be between 1 and 5");
        }
        if (this.reviewCacheService.get(review.getPurchaseId()) != null) {
            throw new ApiException(HttpServletResponse.SC_BAD_REQUEST, String
                    .format("There is already a review for the purchase %s", review.getPurchaseId()));
        }
        CompletableFuture<Void> createInRepository = AsyncUtils.run(() -> this.reviewRepository.create(review));
        CompletableFuture<Void> createInCache = AsyncUtils.run(() -> this.reviewCacheService.save(review));
        AsyncUtils.waitFor(createInRepository, createInCache);
        return review;
    }

    @Override
    public Review modify(Review reviewToModify) {
        Review previousReview = this.reviewCacheService.get(reviewToModify.getPurchaseId());
        if (previousReview == null) {
            throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "Review not found");
        }
        CompletableFuture<Void> updateOnRepository = AsyncUtils.run(() -> this.reviewRepository.update(reviewToModify));
        CompletableFuture<Void> updateOnCache = AsyncUtils.run(() -> this.reviewCacheService.save(reviewToModify));
        AsyncUtils.waitFor(updateOnRepository, updateOnCache);
        return reviewToModify;
    }

    @Override
    public Review deleteForPurchase(String purchaseId) {
        Review reviewToDelete = this.reviewCacheService.get(purchaseId);
        if (reviewToDelete == null) {
            throw new ApiException(HttpServletResponse.SC_NOT_FOUND, "Review not found");
        }
        CompletableFuture<Void> deleteFromRepository = AsyncUtils.run(() ->
                this.reviewRepository.deleteForPurchase(purchaseId));
        CompletableFuture<Void> deleteFromCache = AsyncUtils.run(() -> this.reviewCacheService.invalidate(purchaseId));
        AsyncUtils.waitFor(deleteFromRepository, deleteFromCache);
        return reviewToDelete;
    }

    @Override
    public Collection<Review> search(Map<String, String[]> params) {
        String shopId = params.get("shop_id")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateFrom = LocalDateTime.parse(params.get("date_from")[0], formatter);
        LocalDateTime dateTo = LocalDateTime.parse(params.get("date_to")[0], formatter);

        Map<String, Object> sanitizedParams = new HashMap<>();
        sanitizedParams.put("shop_id", shopId);
        sanitizedParams.put("date_from", dateFrom);
        sanitizedParams.put("date_to", dateTo);

        // TODO: MAKE THIS MORE GENERIC
        return this.reviewRepository.search(sanitizedParams);
    }
}
