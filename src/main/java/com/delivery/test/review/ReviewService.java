package com.pedidosya.test.review;

import java.util.Collection;
import java.util.Map;

public interface ReviewService {
    /**
     *
     * @param reviewId
     * @return retrieved {@link Review}
     */
    Review get(String reviewId);

    /**
     *
     * @param purchaseId
     * @return retrieved {@link Review} associated to purchaseId
     */
    Review getByPurchase(String purchaseId);

    /**
     *
     * @param review
     * @return created {@link Review}
     */
    Review create(Review review);

    /**
     *
     * @param review to modify
     * @return modified {@link Review}
     */
    Review modify(Review review);

    /**
     *
     * @param purchaseId
     * @return deleted {@link Review} associated to purchaseId
     */
    Review deleteForPurchase(String purchaseId);

    /**
     *
     * @param params
     * @return Collection of {@link Review} associated to params
     */
    Collection<Review> search(Map<String, String[]> params);



}
