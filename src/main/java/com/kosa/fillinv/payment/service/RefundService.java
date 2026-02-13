package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.payment.client.TossPaymentClient;
import com.kosa.fillinv.payment.client.dto.PaymentCancelCommand;
import com.kosa.fillinv.payment.domain.PSPConfirmationException;
import com.kosa.fillinv.payment.domain.PaymentFailure;
import com.kosa.fillinv.payment.domain.RefundExecutionResult;
import com.kosa.fillinv.payment.entity.RefundStatus;
import com.kosa.fillinv.payment.service.dto.PaymentRefundCommand;
import com.kosa.fillinv.payment.service.dto.PaymentRefundResult;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import com.kosa.fillinv.payment.service.dto.RefundStatusUpdateCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.sql.SQLException;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundCommandService refundCommandService;
    private final TossPaymentClient tossPaymentClient;

    public PaymentRefundResult refund(PaymentRefundCommand command) {

        RefundDTO refund = refundCommandService.createRefund(PaymentRefundCommand.toRefundCreateCommand(command));

        try {
            refundCommandService.updateStatus(
                    new RefundStatusUpdateCommand(
                            refund.refundId(),
                            RefundStatus.EXECUTING,
                            null,
                            null
                    )
            );

            RefundExecutionResult result = tossPaymentClient.cancel(
                    new PaymentCancelCommand(refund.paymentKey(), refund.orderId(), refund.refundReason(), refund.refundAmount()));

            refundCommandService.updateStatus(
                    new RefundStatusUpdateCommand(
                            refund.refundId(),
                            RefundStatus.SUCCESS,
                            result.refundExtraDetails(),
                            null
                    )
            );

            return new PaymentRefundResult(RefundStatus.SUCCESS, null);
        } catch (Exception e) {
            return handleRefundError(refund, e);
        }
    }

    public PaymentRefundResult handleRefundError(RefundDTO refundDTO, Throwable e) {
        RefundStatus status;
        PaymentFailure failure;

        if (e instanceof PSPConfirmationException) {
            status = RefundStatus.fromName(((PSPConfirmationException) e).paymentStatus().name());
            failure = new PaymentFailure(((PSPConfirmationException) e).getErrorCode(), e.getMessage());
        } else if (e instanceof SQLException) { // Todo TOSS confirm api는 성공하고 내부 서버에서 상태 저장에 실패한 경우 (PaymentStatus.EXECUTING) 별도 처리 필요
            status = RefundStatus.UNKNOWN;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "" : e.getMessage());
        } else if (e instanceof ResourceAccessException) { // time out or network
            status = RefundStatus.UNKNOWN;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "" : e.getMessage());
        } else {
            status = RefundStatus.FAILURE;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "" : e.getMessage());
        }

        refundCommandService.updateStatus(
                new RefundStatusUpdateCommand(
                        refundDTO.refundId(),
                        status,
                        null,
                        failure
                )
        );

        return new PaymentRefundResult(status, failure);
    }

}
