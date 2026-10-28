package org.jala.edu.core.contract;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductInfo(UUID productId, UUID commerceId, String name, BigDecimal price) {
}
