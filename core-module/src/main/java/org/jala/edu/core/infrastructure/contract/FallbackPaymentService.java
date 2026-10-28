package org.jala.edu.core.infrastructure.contract;

import java.math.BigDecimal;
import java.util.UUID;
import org.jala.edu.core.contract.PaymentResult;
import org.jala.edu.core.contract.PaymentService;

/**
 * Stand-in {@link PaymentService}: approves amounts above zero up to {@link #MAX_AMOUNT}, so callers can try
 * both outcomes. The real approval rule belongs to {@code payment-module}.
 */
public class FallbackPaymentService implements PaymentService {

    public static final BigDecimal MAX_AMOUNT = new BigDecimal("1000.00");

    @Override
    public PaymentResult pay(UUID orderId, UUID userId, BigDecimal amount) {
        String reference = "FALLBACK-" + orderId;
        if (amount == null || amount.signum() <= 0) {
            return new PaymentResult(reference, false, "Amount must be greater than zero");
        }
        if (amount.compareTo(MAX_AMOUNT) > 0) {
            return new PaymentResult(reference, false, "Amount exceeds " + MAX_AMOUNT);
        }
        return new PaymentResult(reference, true, "");
    }
}
