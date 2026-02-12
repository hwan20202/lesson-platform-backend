package com.kosa.fillinv.payment.service.dto;

import com.kosa.fillinv.payment.client.TossPaymentClient;
import com.kosa.fillinv.payment.client.dto.PaymentCancelCommand;
import com.kosa.fillinv.payment.domain.PSPConfirmationException;
import com.kosa.fillinv.payment.domain.RefundExecutionResult;
import com.kosa.fillinv.payment.domain.RefundExtraDetails;
import com.kosa.fillinv.payment.entity.RefundStatus;
import com.kosa.fillinv.payment.service.RefundCommandService;
import com.kosa.fillinv.payment.service.RefundService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefundServiceTest {

    @Mock
    private RefundCommandService refundCommandService;

    @Mock
    private TossPaymentClient tossPaymentClient;

    @InjectMocks
    private RefundService refundService;

    private static PaymentRefundCommand mockCommand() {
        String paymentId = "paymentId";
        String cancelReason = "orderId";
        int refundAmount = 1000;

        return new PaymentRefundCommand(paymentId, cancelReason, refundAmount);
    }

    @Test
    @DisplayName("결제 취소 요청 시 Refund 객체가 생성된다.")
    void createRefund_whenPaymentCancelRequested() {
        PaymentRefundCommand command = mockCommand();

        when(refundCommandService.createRefund(any()))
                .thenReturn(mockReturnDTO());

        refundService.refund(command);

        verify(refundCommandService).createRefund(
                argThat(cmd ->
                        cmd.paymentId().equals(command.paymentId()) &&
                                cmd.cancelReason().equals(command.cancelReason()) &&
                                cmd.refundAmount().equals(command.refundAmount())
                )
        );
    }

    @Test
    @DisplayName("결제 취소 api 호출 직전에 Refund 객체 상태가 EXECUTING으로 변경된다.")
    void updateStatusToExecuting_beforeCallTossPaymentCancelApi() {
        PaymentRefundCommand command = mockCommand();

        when(refundCommandService.createRefund(any()))
                .thenReturn(mockReturnDTO());

        refundService.refund(command);

        InOrder inOrder = inOrder(refundCommandService, tossPaymentClient);

        inOrder.verify(refundCommandService)
                .updateStatus(argThat(cmd ->
                        cmd.status() == RefundStatus.EXECUTING));

        inOrder.verify(tossPaymentClient)
                .cancel(any(PaymentCancelCommand.class));
    }

    private RefundDTO mockReturnDTO() {
        return mock(RefundDTO.class);
    }

    @Test
    @DisplayName("결제 취소 api가 성공하면 Refund 객체 상태가 SUCCESS로 변경된다.")
    void updateStatusToSuccess_afterTossPaymentCancelApiSuccess() {

        PaymentRefundCommand command = mockCommand();

        RefundExecutionResult result = mockSuccessResult();

        when(refundCommandService.createRefund(any()))
                .thenReturn(mockReturnDTO());
        when(tossPaymentClient.cancel(any(PaymentCancelCommand.class)))
                .thenReturn(result);

        refundService.refund(command);

        verify(refundCommandService).updateStatus(argThat(cmd ->
                cmd.status() == RefundStatus.SUCCESS));
    }

    @Test
    @DisplayName("결제 취소 api가 실패하면 Refund 객체 상태가 Failure로 변경된다.")
    void updateStatusToFailure_afterTossPaymentCancelApiFail() {
        PaymentRefundCommand command = mockCommand();

        when(refundCommandService.createRefund(any()))
                .thenReturn(mockReturnDTO());
        when(tossPaymentClient.cancel(any(PaymentCancelCommand.class)))
                .thenThrow(mockPSPConfirmationExceptionFail());

        refundService.refund(command);

        verify(refundCommandService).updateStatus(argThat(cmd ->
                cmd.status() == RefundStatus.FAILURE));
    }

    @Test
    @DisplayName("결제 취소 api 결과를 알 수 없으면 Refund 객체 상태가 Unknown으로 변경된다.")
    void updateStatusToUnknown_afterTossPaymentCancelApiUnknownResult() {
        PaymentRefundCommand command = mockCommand();

        when(refundCommandService.createRefund(any()))
                .thenReturn(mockReturnDTO());
        when(tossPaymentClient.cancel(any(PaymentCancelCommand.class)))
                .thenThrow(mockPSPConfirmationExceptionUnknown());

        refundService.refund(command);

        verify(refundCommandService).updateStatus(argThat(cmd ->
                cmd.status() == RefundStatus.UNKNOWN));
    }

    @Test
    @DisplayName("Refund 객체의 상태가 업데이트되면 RefundHistory가 생성된다.")
    void createRefundHistory_whenRefundStatusUpdated() {

    }

    @Test
    @DisplayName("결제 취소가 성공하면 스케쥴의 상태가 취소로 변경된다")
    void updateScheduleStatusToCanceled_whenRefundSuccess() {

    }

    private PSPConfirmationException mockPSPConfirmationExceptionUnknown() {
        return PSPConfirmationException.builder()
                .errorCode("400")
                .errorMessage("알 수 없음")
                .isSuccess(false)
                .isFailure(false)
                .isUnknown(true)
                .isRetryable(true)
                .build();
    }

    private PSPConfirmationException mockPSPConfirmationExceptionFail() {
        return PSPConfirmationException.builder()
                .errorCode("400")
                .errorMessage("실패")
                .isSuccess(false)
                .isFailure(true)
                .isUnknown(false)
                .isRetryable(false)
                .build();
    }

    private RefundExecutionResult mockSuccessResult() {
        return new RefundExecutionResult(
                "paymentKey",
                "orderId",
                new RefundExtraDetails()
        );
    }

}