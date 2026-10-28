package org.jala.edu.core.infrastructure.contract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.List;
import java.util.UUID;
import org.jala.edu.core.contract.ReadyOrder;
import org.jala.edu.core.domain.exception.ConflictException;
import org.jala.edu.core.domain.exception.NotFoundException;
import org.junit.jupiter.api.Test;

class FallbackOrderServiceTest {

    private final FallbackOrderService orderService = new FallbackOrderService();

    @Test
    void startsWithThreeReadyOrders() {
        // Arrange (fresh service)

        // Act
        List<ReadyOrder> ready = orderService.findReadyForDelivery();

        // Assert
        assertThat(ready).hasSize(3);
    }

    @Test
    void anAssignedOrderLeavesTheReadyListAndCannotBeTakenTwice() {
        // Arrange
        UUID orderId = firstReadyOrder();
        orderService.assign(orderId, UUID.randomUUID());

        // Act
        Throwable thrown = catchThrowable(() -> orderService.assign(orderId, UUID.randomUUID()));

        // Assert
        assertThat(thrown).isInstanceOf(ConflictException.class);
        assertThat(orderService.findReadyForDelivery()).extracting(ReadyOrder::orderId).doesNotContain(orderId);
    }

    @Test
    void followsTheDeliveryStepsInOrder() {
        // Arrange
        UUID orderId = firstReadyOrder();

        // Act
        Throwable skipped = catchThrowable(() -> orderService.markDelivered(orderId));
        orderService.assign(orderId, UUID.randomUUID());
        orderService.markOnTheWay(orderId);
        orderService.markDelivered(orderId);

        // Assert
        assertThat(skipped).isInstanceOf(ConflictException.class);
    }

    @Test
    void throwsWhenTheOrderDoesNotExist() {
        // Arrange
        UUID unknown = UUID.randomUUID();

        // Act
        Throwable thrown = catchThrowable(() -> orderService.markOnTheWay(unknown));

        // Assert
        assertThat(thrown).isInstanceOf(NotFoundException.class);
    }

    private UUID firstReadyOrder() {
        return orderService.findReadyForDelivery().getFirst().orderId();
    }
}
