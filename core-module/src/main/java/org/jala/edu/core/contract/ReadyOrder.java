package org.jala.edu.core.contract;

import java.math.BigDecimal;
import java.util.UUID;

public record ReadyOrder(UUID orderId, String deliveryAddress, BigDecimal shippingCost) {
}
