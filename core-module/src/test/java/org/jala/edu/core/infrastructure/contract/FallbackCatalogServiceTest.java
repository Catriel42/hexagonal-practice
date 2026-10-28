package org.jala.edu.core.infrastructure.contract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import java.util.UUID;
import org.jala.edu.core.contract.SampleUsers;
import org.jala.edu.core.domain.exception.ConflictException;
import org.jala.edu.core.domain.exception.NotFoundException;
import org.junit.jupiter.api.Test;

class FallbackCatalogServiceTest {

    private final FallbackCatalogService catalogService = new FallbackCatalogService();

    @Test
    void availabilityDependsOnTheStock() {
        // Arrange
        UUID product = FallbackCatalogService.AVAILABLE_PRODUCT;

        // Act
        boolean withinStock = catalogService.isAvailable(product, 10);
        boolean aboveStock = catalogService.isAvailable(product, 11);
        boolean soldOut = catalogService.isAvailable(FallbackCatalogService.SOLD_OUT_PRODUCT, 1);

        // Assert
        assertThat(withinStock).isTrue();
        assertThat(aboveStock).isFalse();
        assertThat(soldOut).isFalse();
    }

    @Test
    void reservingLowersTheStockAndReleasingRestoresIt() {
        // Arrange
        UUID product = FallbackCatalogService.AVAILABLE_PRODUCT;

        // Act
        catalogService.reserveStock(product, 8);
        boolean afterReserve = catalogService.isAvailable(product, 3);
        catalogService.releaseStock(product, 8);

        // Assert
        assertThat(afterReserve).isFalse();
        assertThat(catalogService.isAvailable(product, 10)).isTrue();
    }

    @Test
    void cannotReserveMoreThanTheStock() {
        // Arrange
        UUID product = FallbackCatalogService.AVAILABLE_PRODUCT;

        // Act
        Throwable thrown = catchThrowable(() -> catalogService.reserveStock(product, 11));

        // Assert
        assertThat(thrown).isInstanceOf(ConflictException.class);
    }

    @Test
    void throwsWhenTheProductDoesNotExist() {
        // Arrange
        UUID unknown = UUID.randomUUID();

        // Act
        Throwable thrown = catchThrowable(() -> catalogService.reserveStock(unknown, 1));

        // Assert
        assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    @Test
    void theSampleMerchantOwnsTheSampleCommerce() {
        // Arrange (fresh service)

        // Act
        List<UUID> merchantCommerces = catalogService.findCommerceIdsByMerchant(SampleUsers.MERCHANT);
        List<UUID> customerCommerces = catalogService.findCommerceIdsByMerchant(SampleUsers.CUSTOMER);

        // Assert
        assertThat(merchantCommerces).containsExactly(FallbackCatalogService.SAMPLE_COMMERCE);
        assertThat(customerCommerces).isEmpty();
    }
}
