package org.jala.edu.core.contract;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Product lookup and stock offered by {@code catalog-module}; {@code order-module} uses it to build and confirm
 * orders and to show each commerce its own orders. The stock operations throw {@code NotFoundException} for an
 * unknown product id.
 */
public interface CatalogService {

    /**
     * @return the product's commerce, name and current price, or empty if no product has that id
     */
    Optional<ProductInfo> findProduct(UUID productId);

    /**
     * A product is available when it is marked available, its commerce is active and its stock covers the quantity.
     */
    boolean isAvailable(UUID productId, int quantity);

    /**
     * Takes the quantity out of the product's stock when the order is confirmed. Throws {@code ConflictException}
     * if the stock does not cover it.
     */
    void reserveStock(UUID productId, int quantity);

    /**
     * Puts the quantity back into the product's stock when a confirmed order is cancelled.
     */
    void releaseStock(UUID productId, int quantity);

    /**
     * @return the commerces owned by that merchant, empty if it owns none
     */
    List<UUID> findCommerceIdsByMerchant(UUID merchantId);
}
