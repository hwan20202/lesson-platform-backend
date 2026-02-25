package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.payment.entity.Refund;
import com.kosa.fillinv.payment.entity.RefundStatus;
import java.time.Instant;

public class RefundBuilder {

    private String id;
    private String paymentId;
    private String paymentKey;
    private String orderId;
    private RefundStatus refundStatus;
    private Integer refundAmount;
    private String refundReason;
    private String transactionKey;
    private Instant refundedAt;
    private Instant createdAt;
    private String pspRaw;

    public RefundBuilder(RefundStatus refundStatus) {
        switch (refundStatus) {
            case RefundStatus.NOT_STARTED -> notStarted();
            case RefundStatus.EXECUTING -> executing();
            case RefundStatus.SUCCESS -> success();
            case RefundStatus.FAILURE -> failure();
            case RefundStatus.UNKNOWN -> unknown();
        }
    }

    public void notStarted() {
        this.id = "refund-001";
        this.paymentId = "payment-001";
        this.paymentKey = "payment-key";
        this.orderId = "order-id";
        this.refundAmount = 1000;
        this.refundReason = "refund reason";
        this.refundStatus = RefundStatus.NOT_STARTED;
        this.createdAt = Instant.now();
    }

    private void executing() {
        notStarted();
        this.refundStatus = RefundStatus.EXECUTING;
    }

    private void success() {
        notStarted();
        this.refundStatus = RefundStatus.SUCCESS;
        this.transactionKey = "transactionKey-001";
        this.refundedAt = Instant.now();
        this.pspRaw = "pspRawData1101";
    }

    private void failure() {
        notStarted();
        this.refundStatus = RefundStatus.FAILURE;
        this.transactionKey = "transactionKey-001";
        this.refundedAt = null;
        this.pspRaw = "pspRawData1101";
    }

    private void unknown() {
        notStarted();
        this.refundStatus = RefundStatus.UNKNOWN;
    }

    public RefundBuilder paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public RefundBuilder paymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
        return this;
    }

    public RefundBuilder orderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public RefundBuilder refundAmount(Integer refundAmount) {
        this.refundAmount = refundAmount;
        return this;
    }

    public RefundBuilder refundReason(String refundReason) {
        this.refundReason = refundReason;
        return this;
    }

    public RefundBuilder transactionKey(String transactionKey) {
        this.transactionKey = transactionKey;
        return this;
    }

    public RefundBuilder refundedAt(Instant refundedAt) {
        this.refundedAt = refundedAt;
        return this;
    }

    public RefundBuilder createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public RefundBuilder pspRaw(String pspRaw) {
        this.pspRaw = pspRaw;
        return this;
    }

    public Refund build() {
        Refund refund = Refund.builder()
                .id(this.id)
                .paymentId(this.paymentId)
                .paymentKey(this.paymentKey)
                .orderId(this.orderId)
                .refundStatus(this.refundStatus)
                .refundAmount(this.refundAmount)
                .refundReason(this.refundReason)
                .build();

        if (refundStatus == RefundStatus.EXECUTING) {
            refund.markExecuting();
        } else if (refundStatus == RefundStatus.SUCCESS) {
            refund.markSuccess(transactionKey, refundedAt, pspRaw);
        } else if (refundStatus == RefundStatus.FAILURE) {
            refund.markFail();
        } else if (refundStatus == RefundStatus.UNKNOWN) {
            refund.markUnknown();
        }

        return refund;
    }
}
