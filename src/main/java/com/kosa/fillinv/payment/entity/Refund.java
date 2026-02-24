package com.kosa.fillinv.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refunds")
public class Refund {
    @Id
    @Column(name = "refund_id")
    private String id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    @Column(name = "refund_amount")
    private Integer refundAmount;

    @Column(name = "refund_reason")
    private String refundReason;

    @Setter
    @Column(name = "transaction_key")
    private String transactionKey;

    @Setter
    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Setter
    @Column(name = "psp_raw")
    private String pspRaw;

    @Builder
    public Refund(String id,
                  String paymentId,
                  String paymentKey,
                  String orderId,
                  RefundStatus refundStatus,
                  Integer refundAmount,
                  String refundReason) {
        this.id = id;
        this.paymentId = paymentId;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.refundStatus = refundStatus;
        this.refundAmount = refundAmount;
        this.refundReason = refundReason;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    public void markExecuting() {
        if (refundStatus == RefundStatus.NOT_STARTED ||
                refundStatus == RefundStatus.UNKNOWN) {

            refundStatus = RefundStatus.EXECUTING;
            return;
        }

        throw new IllegalStateException(
                "EXECUTING 상태로 변경할 수 없습니다. 현재 상태: " + refundStatus);
    }

    public void markSuccess(String transactionKey, Instant refundedAt, String pspRaw) {
        if (refundStatus == RefundStatus.EXECUTING ||
                refundStatus == RefundStatus.UNKNOWN) {

            this.refundStatus = RefundStatus.SUCCESS;
            this.transactionKey = transactionKey;
            this.refundedAt = refundedAt;
            this.pspRaw = pspRaw;
            return;
        }

        throw new IllegalStateException(
                "SUCCESS 상태로 변경할 수 없습니다. 현재 상태: " + refundStatus);
    }

    public void markFail() {
        if (refundStatus == RefundStatus.NOT_STARTED ||
                refundStatus == RefundStatus.EXECUTING ||
                refundStatus == RefundStatus.UNKNOWN) {

            refundStatus = RefundStatus.FAILURE;
            return;
        }

        throw new IllegalStateException(
                "FAILURE 상태로 변경할 수 없습니다. 현재 상태: " + refundStatus);
    }

    public void markUnknown() {
        if (refundStatus == RefundStatus.NOT_STARTED ||
                refundStatus == RefundStatus.EXECUTING) {

            refundStatus = RefundStatus.UNKNOWN;
            return;
        }

        throw new IllegalStateException(
                "UNKNOWN 상태로 변경할 수 없습니다. 현재 상태: " + refundStatus);
    }
}
