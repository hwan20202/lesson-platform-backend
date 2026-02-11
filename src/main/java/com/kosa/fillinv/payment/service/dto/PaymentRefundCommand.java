package com.kosa.fillinv.payment.service.dto;

public record PaymentRefundCommand(
        String paymentKey,
        String cancelReason,
        Integer refundAmount
) {
}
