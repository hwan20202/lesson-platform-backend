package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.global.exception.ResourceException;
import com.kosa.fillinv.payment.entity.*;
import com.kosa.fillinv.payment.repository.PaymentRepository;
import com.kosa.fillinv.payment.repository.RefundHistoryRepository;
import com.kosa.fillinv.payment.repository.RefundRepository;
import com.kosa.fillinv.payment.service.dto.RefundCreateCommand;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import com.kosa.fillinv.payment.service.dto.RefundStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundCommandService {

    private final RefundRepository refundRepository;
    private final PaymentRepository paymentRepository;
    private final RefundHistoryRepository refundHistoryRepository;

    public RefundDTO createRefund(RefundCreateCommand command) {

        Payment payment = paymentRepository.findById(command.paymentId())
                .orElseThrow(() -> new ResourceException.NotFound("결제 정보가 존재하지 않습니다."));

        Refund newRefund = Refund.builder()
                .id(UUID.randomUUID().toString())
                .paymentId(command.paymentId())
                .paymentKey(payment.getPaymentKey())
                .orderId(payment.getOrderId())
                .refundAmount(command.refundAmount())
                .refundReason(command.cancelReason())
                .refundStatus(RefundStatus.NOT_STARTED)
                .build();

        Refund saved = refundRepository.save(newRefund);

        return RefundDTO.of(saved);
    }

    @Transactional
    public void updateStatus(RefundStatusUpdateCommand command) {
        switch (command.status()) {
            case EXECUTING -> execute(command);
            case SUCCESS -> success(command);
            case FAILURE -> fail(command);
            case UNKNOWN -> unknown(command);
            default -> throw new IllegalArgumentException("Unknown status: " + command.status());
        }
    }

    private void unknown(RefundStatusUpdateCommand command) {
        Refund refund = refundRepository.findById(command.refundId())
                .orElseThrow(() -> new ResourceException.NotFound("환불 정보 없음"));

        RefundHistory refundHistory = createRefundHistory(refund, RefundStatus.UNKNOWN, command.failure() == null ? null : command.failure().toString());
        refundHistoryRepository.save(refundHistory);

        refund.markUnknown();
    }

    private void fail(RefundStatusUpdateCommand command) {
        Refund refund = refundRepository.findById(command.refundId())
                .orElseThrow(() -> new ResourceException.NotFound("환불 정보 없음"));

        RefundHistory refundHistory = createRefundHistory(refund, RefundStatus.FAILURE, command.failure() == null ? null : command.failure().toString());
        refundHistoryRepository.save(refundHistory);

        refund.markFail();
    }

    private void success(RefundStatusUpdateCommand command) {
        Refund refund = refundRepository.findById(command.refundId())
                .orElseThrow(() -> new ResourceException.NotFound("환불 정보 없음"));

        RefundHistory refundHistory = createRefundHistory(refund, RefundStatus.SUCCESS, "PAYMENT_CANCELATION_DONE");
        refundHistoryRepository.save(refundHistory);

        refund.markSuccess(
                command.extraDetails().transactionKey(),
                command.extraDetails().refundedAt(),
                command.extraDetails().pspRawData()
        );
    }

    private void execute(RefundStatusUpdateCommand command) {
        Refund refund = refundRepository.findById(command.refundId())
                .orElseThrow(() -> new ResourceException.NotFound("환불 정보 없음"));

        RefundHistory refundHistory = createRefundHistory(refund, RefundStatus.EXECUTING, "PAYMENT_CANCELATION_START");
        refundHistoryRepository.save(refundHistory);

        refund.markExecuting();
    }

    private RefundHistory createRefundHistory(Refund refund, RefundStatus newStatus, String reason) {
        return RefundHistory.builder()
                .id(UUID.randomUUID().toString())
                .paymentKey(refund.getPaymentKey())
                .previousStatus(refund.getRefundStatus())
                .newStatus(newStatus)
                .reason(reason)
                .build();
    }
}
