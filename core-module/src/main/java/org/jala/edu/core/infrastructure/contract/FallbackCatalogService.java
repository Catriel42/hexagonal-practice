package org.jala.edu.core.infrastructure.contract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jala.edu.core.contract.CatalogService;
import org.jala.edu.core.contract.ProductInfo;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.core.domain.exception.ConflictException;
import org.jala.edu.core.domain.exception.NotFoundException;

/**
 * Stand-in {@link CatalogService} with one commerce owned by {@link SampleUsers#MERCHANT} and two products: one
 * available with stock 10 and one sold out. Stock changes are kept in memory and lost when the application stops.
 */
public class FallbackCatalogService implements CatalogService {

    public static final UUID SAMPLE_COMMERCE = UUID.fromString("00000000-0000-0000-0000-000000000301");
    public static final UUID AVAILABLE_PRODUCT = UUID.fromString("00000000-0000-0000-0000-000000000101");
    public static final UUID SOLD_OUT_PRODUCT = UUID.fromString("00000000-0000-0000-0000-000000000102");

    private static final int INITIAL_STOCK = 10;

    private static final Map<UUID, ProductInfo> PRODUCTS = Map.of(
            AVAILABLE_PRODUCT, new ProductInfo(AVAILABLE_PRODUCT, SAMPLE_COMMERCE, "Pizza", new BigDecimal("50.00")),
            SOLD_OUT_PRODUCT, new ProductInfo(SOLD_OUT_PRODUCT, SAMPLE_COMMERCE, "Empanada", new BigDecimal("8.50")));

    private final Map<UUID, Integer> stock = new ConcurrentHashMap<>(Map.of(AVAILABLE_PRODUCT, INITIAL_STOCK));

    @Override
    public Optional<ProductInfo> findProduct(UUID productId) {
        return Optional.ofNullable(PRODUCTS.get(productId));
    }

    @Override
    public boolean isAvailable(UUID productId, int quantity) {
        return quantity > 0 && stock.getOrDefault(productId, 0) >= quantity;
    }

    @Override
    public void reserveStock(UUID productId, int quantity) {
        requireProduct(productId);
        if (!isAvailable(productId, quantity)) {
            throw new ConflictException("Not enough stock for product " + productId);
        }
        stock.merge(productId, -quantity, Integer::sum);
    }

    @Override
    public void releaseStock(UUID productId, int quantity) {
        requireProduct(productId);
        stock.merge(productId, quantity, Integer::sum);
    }

    @Override
    public List<UUID> findCommerceIdsByMerchant(UUID merchantId) {
        return SampleUsers.MERCHANT.equals(merchantId) ? List.of(SAMPLE_COMMERCE) : List.of();
    }

    private void requireProduct(UUID productId) {
        if (!PRODUCTS.containsKey(productId)) {
            throw new NotFoundException("Product not found: " + productId);
        }
    }
}
