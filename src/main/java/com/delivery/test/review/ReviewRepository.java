package com.pedidosya.test.review;

import java.util.Collection;
import java.util.Map;

public interface ReviewRepository {

    /**
     *
     * @param purchaseId
     * @return {@link Review} associated to purchaseId
     */
    Review getByPurchaseId(String purchaseId);

    /**
     * Deletes the {@link Review} associated with the purchaseId
     * @param purchaseId
     */
    void deleteForPurchase(String purchaseId);

    /**
     *
     * @param review
     * @return created {@link Review}
     */
    Review create(Review review);

    /**
     *
     * @param review
     * @return updated {@link Review}
     */
    Review update(Review review);

    /**
     *
     * @param params
     * @return Collection of {@link Review} matching params
     */
    Collection<Review> search(Map<String, Object> params);
}
