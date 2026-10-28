package org.jala.edu.core.contract;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Simulated payment offered by {@code payment-module}; {@code order-module} calls it when an order is confirmed.
 */
public interface PaymentService {

    PaymentResult pay(UUID orderId, UUID userId, BigDecimal amount);
}
