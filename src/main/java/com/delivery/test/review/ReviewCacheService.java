package com.pedidosya.test.review;

public interface ReviewCacheService {

    /**
     *
     * @param purchaseId
     * @return recovered review from cache
     */
    Review get(String purchaseId);

    /**
     *
     * @param purchaseId
     */
    void invalidate(String purchaseId);

    /**
     * Stores the review in the cache using de purchase id as the key
     * @param review
     * @return
     */
    Review save(Review review);
}
