package org.jala.edu.core.contract;

/**
 * @param reason why the payment was rejected; empty when it was approved
 */
public record PaymentResult(String paymentReference, boolean approved, String reason) {
}
