package org.jala.edu.core.infrastructure.contract;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.jala.edu.core.contract.OrderService;
import org.jala.edu.core.contract.ReadyOrder;
import org.jala.edu.core.domain.exception.ConflictException;
import org.jala.edu.core.domain.exception.NotFoundException;

/**
 * Stand-in {@link OrderService} that starts with three orders ready for delivery and enforces the delivery steps
 * in memory. State is lost when the application stops.
 */
public class FallbackOrderService implements OrderService {

    private static final BigDecimal SHIPPING_COST = new BigDecimal("10.00");

    private final Map<UUID, ReadyOrder> orders = new ConcurrentHashMap<>();
    private final Map<UUID, Step> steps = new ConcurrentHashMap<>();

    public FallbackOrderService() {
        addReadyOrder("00000000-0000-0000-0000-000000000201", "Av. Siempre Viva 742");
        addReadyOrder("00000000-0000-0000-0000-000000000202", "Calle Falsa 123");
        addReadyOrder("00000000-0000-0000-0000-000000000203", "Plaza Principal 1");
    }

    @Override
    public List<ReadyOrder> findReadyForDelivery() {
        return orders.values().stream()
                .filter(order -> steps.get(order.orderId()) == Step.READY)
                .toList();
    }

    @Override
    public void assign(UUID orderId, UUID courierId) {
        advance(orderId, Step.READY, Step.ASSIGNED);
    }

    @Override
    public void markOnTheWay(UUID orderId) {
        advance(orderId, Step.ASSIGNED, Step.ON_THE_WAY);
    }

    @Override
    public void markDelivered(UUID orderId) {
        advance(orderId, Step.ON_THE_WAY, Step.DELIVERED);
    }

    private void addReadyOrder(String id, String address) {
        UUID orderId = UUID.fromString(id);
        orders.put(orderId, new ReadyOrder(orderId, address, SHIPPING_COST));
        steps.put(orderId, Step.READY);
    }

    private void advance(UUID orderId, Step expected, Step next) {
        if (!steps.containsKey(orderId)) {
            throw new NotFoundException("Order not found: " + orderId);
        }
        if (!steps.replace(orderId, expected, next)) {
            throw new ConflictException("Order " + orderId + " is " + steps.get(orderId) + ", expected " + expected);
        }
    }

    private enum Step {
        READY,
        ASSIGNED,
        ON_THE_WAY,
        DELIVERED
    }
}
