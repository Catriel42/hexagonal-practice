package org.jala.edu.core.contract;

import java.util.List;
import java.util.UUID;

/**
 * Delivery side of the order state machine, offered by {@code order-module} to {@code delivery-module}.
 * An unknown order id throws {@code NotFoundException}; a step out of order throws {@code ConflictException}.
 */
public interface OrderService {

    List<ReadyOrder> findReadyForDelivery();

    /**
     * Ready for delivery → Assigned. Fails if another courier already took the order.
     */
    void assign(UUID orderId, UUID courierId);

    /**
     * Assigned → On the way.
     */
    void markOnTheWay(UUID orderId);

    /**
     * On the way → Delivered.
     */
    void markDelivered(UUID orderId);
}
