package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.global.exception.ResourceException;
import com.kosa.fillinv.payment.domain.PaymentFailure;
import com.kosa.fillinv.payment.domain.RefundExtraDetails;
import com.kosa.fillinv.payment.entity.Payment;
import com.kosa.fillinv.payment.entity.Refund;
import com.kosa.fillinv.payment.entity.RefundHistory;
import com.kosa.fillinv.payment.entity.RefundStatus;
import com.kosa.fillinv.payment.repository.PaymentRepository;
import com.kosa.fillinv.payment.repository.RefundHistoryRepository;
import com.kosa.fillinv.payment.repository.RefundRepository;
import com.kosa.fillinv.payment.service.dto.RefundCreateCommand;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import com.kosa.fillinv.payment.service.dto.RefundStatusUpdateCommand;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
@Transactional
class RefundCommandServiceTest {

    @Autowired
    private RefundCommandService refundCommandService;

    @MockitoBean
    private PaymentRepository paymentRepository;

    @MockitoSpyBean
    private RefundRepository refundRepository;

    @Autowired
    private RefundHistoryRepository refundHistoryRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("Refund 이벤트를 생성한다.")
    void createRefund() {
        // given
        String paymentId = "payment-001";
        String refundReason = "refund reason";
        Integer refundAmount = 1000;
        String orderId = "order-id";
        String paymentKey = "payment-key";

        RefundCreateCommand command = new RefundCreateCommand(paymentId, refundReason, refundAmount);

        Payment payment = Payment.builder()
                .id(paymentId)
                .orderId(orderId)
                .build();

        payment.setPaymentKey(paymentKey);

        when(paymentRepository.findById(paymentId))
                .thenReturn(Optional.of(payment));

        // when
        RefundDTO refund = refundCommandService.createRefund(command);

        // then
        assertThat(refund.paymentId()).isEqualTo(payment.getId());
        assertThat(refund.paymentKey()).isEqualTo(payment.getPaymentKey());
        assertThat(refund.orderId()).isEqualTo(payment.getOrderId());
        assertThat(refund.refundAmount()).isEqualTo(refundAmount);
        assertThat(refund.refundReason()).isEqualTo(refundReason);
        assertThat(refund.refundStatus()).isEqualTo(RefundStatus.NOT_STARTED);
    }

    @Test
    @DisplayName("payment가 존재하지 않으면 예외를 반환한다.")
    void createRefundFail_whenPaymentNotFound() {
        // given
        String paymentId = "payment-001";
        String refundReason = "refund reason";
        Integer refundAmount = 1000;

        RefundCreateCommand command = new RefundCreateCommand(paymentId, refundReason, refundAmount);

        when(paymentRepository.findById(paymentId))
                .thenThrow(mock(ResourceException.NotFound.class));

        // when, then
        assertThrows(
                ResourceException.NotFound.class,
                () -> refundCommandService.createRefund(command)
        );

        verify(refundRepository, never()).save(any(Refund.class));
    }

    @Test
    @DisplayName("결제 상태를 결제 진행중으로 변경한다.")
    void updateStatusToExecuting() {
        // given
        String refundId = "refund-001";
        String paymentKey = "payment-key";

        refundRepository.save(
                Refund.builder()
                        .id(refundId)
                        .paymentId("payment-001")
                        .paymentKey(paymentKey)
                        .orderId("order-id")
                        .refundStatus(RefundStatus.NOT_STARTED)
                        .refundAmount(1000)
                        .refundReason("refund reason")
                        .build()
        );
        entityManager.flush(); entityManager.clear();

        RefundStatusUpdateCommand command = new RefundStatusUpdateCommand(
                refundId,
                RefundStatus.EXECUTING,
                null,
                null
        );

        // when
        refundCommandService.updateStatus(command);
        entityManager.flush(); entityManager.clear();

        // then
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new AssertionError("Refund가 저장되지 않았습니다."));

        assertThat(refund.getRefundStatus()).isEqualTo(RefundStatus.EXECUTING);

        RefundHistory history = refundHistoryRepository.findByPaymentKey(paymentKey).getFirst();

        assertThat(history.getPreviousStatus()).isEqualTo(RefundStatus.NOT_STARTED);
        assertThat(history.getNewStatus()).isEqualTo(RefundStatus.EXECUTING);
    }

    @Test
    @DisplayName("결제 상태를 결제 성공으로 변경한다.")
    void updateStatusToSuccess() {
        // given
        String refundId = "refund-001";
        String paymentKey = "payment-key";
        Integer refundAmount = 1000;
        String refundReason = "refund reason";
        Instant refundedAt = Instant.now();
        String transactionKey = "transactionKey-001";
        String pspRaw = "pspRawData1101";

        refundRepository.save(
                Refund.builder()
                        .id(refundId)
                        .paymentId("payment-001")
                        .paymentKey(paymentKey)
                        .orderId("order-id")
                        .refundStatus(RefundStatus.EXECUTING)
                        .refundAmount(refundAmount)
                        .refundReason(refundReason)
                        .build()
        );
        entityManager.flush(); entityManager.clear();

        RefundStatusUpdateCommand command = new RefundStatusUpdateCommand(
                refundId,
                RefundStatus.SUCCESS,
                new RefundExtraDetails(
                        refundedAt,
                        refundAmount,
                        refundReason,
                        transactionKey,
                        pspRaw
                ),
                null
        );

        // when
        refundCommandService.updateStatus(command);
        entityManager.flush(); entityManager.clear();

        // then
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new AssertionError("Refund가 저장되지 않았습니다."));

        assertThat(refund.getRefundStatus()).isEqualTo(RefundStatus.SUCCESS);
        assertThat(refund.getRefundedAt()).isEqualTo(refundedAt);
        assertThat(refund.getRefundAmount()).isEqualTo(refundAmount);
        assertThat(refund.getTransactionKey()).isEqualTo(transactionKey);
        assertThat(refund.getPspRaw()).isEqualTo(pspRaw);

        RefundHistory history = refundHistoryRepository.findByPaymentKey(paymentKey).getFirst();

        assertThat(history.getPreviousStatus()).isEqualTo(RefundStatus.EXECUTING);
        assertThat(history.getNewStatus()).isEqualTo(RefundStatus.SUCCESS);
    }

    @Test
    @DisplayName("결제 상태를 결제 실패로 변경한다.")
    void updateStatusToFailure() {
        // given
        String refundId = "refund-001";
        String paymentKey = "payment-key";
        Integer refundAmount = 1000;
        String refundReason = "refund reason";
        PaymentFailure failure = new PaymentFailure("400", "결제 실패");

        refundRepository.save(
                Refund.builder()
                        .id(refundId)
                        .paymentId("payment-001")
                        .paymentKey(paymentKey)
                        .orderId("order-id")
                        .refundStatus(RefundStatus.EXECUTING)
                        .refundAmount(refundAmount)
                        .refundReason(refundReason)
                        .build()
        );
        entityManager.flush(); entityManager.clear();

        RefundStatusUpdateCommand command = new RefundStatusUpdateCommand(
                refundId,
                RefundStatus.FAILURE,
                null,
                failure
        );

        // when
        refundCommandService.updateStatus(command);
        entityManager.flush(); entityManager.clear();

        // then
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new AssertionError("Refund가 저장되지 않았습니다."));

        assertThat(refund.getRefundStatus()).isEqualTo(RefundStatus.FAILURE);

        RefundHistory history = refundHistoryRepository.findByPaymentKey(paymentKey).getFirst();

        assertThat(history.getPreviousStatus()).isEqualTo(RefundStatus.EXECUTING);
        assertThat(history.getNewStatus()).isEqualTo(RefundStatus.FAILURE);
        assertThat(history.getReason()).isEqualTo(failure.toString());
    }

    @Test
    @DisplayName("결제 상태를 알 수 없음으로 변경한다.")
    void updateStatusToUnknown() {
        // given
        String refundId = "refund-001";
        String paymentKey = "payment-key";
        Integer refundAmount = 1000;
        String refundReason = "refund reason";
        PaymentFailure failure = new PaymentFailure("400", "결제 실패");

        refundRepository.save(
                Refund.builder()
                        .id(refundId)
                        .paymentId("payment-001")
                        .paymentKey(paymentKey)
                        .orderId("order-id")
                        .refundStatus(RefundStatus.EXECUTING)
                        .refundAmount(refundAmount)
                        .refundReason(refundReason)
                        .build()
        );
        entityManager.flush(); entityManager.clear();

        RefundStatusUpdateCommand command = new RefundStatusUpdateCommand(
                refundId,
                RefundStatus.UNKNOWN,
                null,
                failure
        );

        // when
        refundCommandService.updateStatus(command);
        entityManager.flush(); entityManager.clear();

        // then
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new AssertionError("Refund가 저장되지 않았습니다."));

        assertThat(refund.getRefundStatus()).isEqualTo(RefundStatus.UNKNOWN);

        RefundHistory history = refundHistoryRepository.findByPaymentKey(paymentKey).getFirst();

        assertThat(history.getPreviousStatus()).isEqualTo(RefundStatus.EXECUTING);
        assertThat(history.getNewStatus()).isEqualTo(RefundStatus.UNKNOWN);
        assertThat(history.getReason()).isEqualTo(failure.toString());
    }
}
