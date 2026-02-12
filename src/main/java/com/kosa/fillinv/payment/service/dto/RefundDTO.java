package com.kosa.fillinv.payment.service.dto;

import com.kosa.fillinv.payment.entity.RefundStatus;

import java.time.Instant;

public record RefundDTO(
        String refundId,
        String paymentId,
        String paymentKey,
        String orderId,
        RefundStatus refundStatus,
        Integer refundAmount,
        String refundReason,
        String transactionKey,
        Instant refundedAt,
        Instant createdAt,
        String pspRaw
) {
}
