package com.pedidosya.test.review;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.TimeUnit;

@Log4j
@Singleton
public class ReviewCacheServiceImpl implements ReviewCacheService {

    private LoadingCache<String, Review> reviewCache;
    private ReviewRepository reviewRepository;

    @Inject
    public ReviewCacheServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Review>() {
                    @Override
                    public Review load(String s) throws Exception {
                        return reviewRepository.getByPurchaseId(s);
                    }
                });
    }

    @Override
    public Review get(String purchaseId) {
        return this.reviewCache.getIfPresent(purchaseId);
    }

    @Override
    public void invalidate(String purchaseId) {
        this.reviewCache.invalidate(purchaseId);
    }

    @Override
    public Review save(Review review) {
        this.reviewCache.put(review.getPurchaseId(), review);
        return review;
    }
}
