package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.payment.client.TossPaymentClient;
import com.kosa.fillinv.payment.client.dto.PaymentCancelCommand;
import com.kosa.fillinv.payment.domain.PSPConfirmationException;
import com.kosa.fillinv.payment.domain.PaymentFailure;
import com.kosa.fillinv.payment.domain.RefundExecutionResult;
import com.kosa.fillinv.payment.domain.RefundRetryPolicy;
import com.kosa.fillinv.payment.entity.Refund;
import com.kosa.fillinv.payment.entity.RefundStatus;
import com.kosa.fillinv.payment.repository.RefundRepository;
import com.kosa.fillinv.payment.service.dto.PaymentRefundResult;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final RefundCommandService refundCommandService;
    private final TossPaymentClient tossPaymentClient;
    private final RefundRetryPolicy refundRetryPolicy;

    public PaymentRefundResult executeRefund(String refundId) {

        Refund refund = refundRepository.findById(refundId)
                .orElseThrow();

        try {
            refundCommandService.execute(refund.getId(), refundRetryPolicy);

            RefundExecutionResult result = tossPaymentClient.cancel(
                    new PaymentCancelCommand(refund.getPaymentKey(),
                            refund.getOrderId(), refund.getRefundReason(), refund.getRefundAmount()));

            refundCommandService.success(refund.getId(), result.refundExtraDetails());

            return new PaymentRefundResult(RefundStatus.SUCCESS, null);
        } catch (Exception e) {
            return handleRefundError(refund, e);
        }
    }

    public PaymentRefundResult handleRefundError(Refund refund, Throwable e) {
        RefundStatus status;
        PaymentFailure failure;

        if (e instanceof PSPConfirmationException) {
            status = ((PSPConfirmationException) e).refundStatus();
            failure = new PaymentFailure(((PSPConfirmationException) e).getErrorCode(), e.getMessage());
        } else if (e instanceof SQLException) { // Todo TOSS confirm api는 성공하고 내부 서버에서 상태 저장에 실패한 경우 (PaymentStatus.EXECUTING) 별도 처리 필요
            status = RefundStatus.UNKNOWN;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "환불 실행 도중 데이터베이스 관련 오류 발생" : e.getMessage());
        } else if (e instanceof ResourceAccessException) { // time out or network
            status = RefundStatus.UNKNOWN;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "환불 실행 도중 외부 연결 오류 발생" : e.getMessage());
        } else {
            status = RefundStatus.FAILURE;
            failure = new PaymentFailure(e.getClass().getSimpleName(), e.getMessage() == null ? "환불 실행 도중 알 수 없는 오류 발생" : e.getMessage());
        }

        if (status == RefundStatus.FAILURE) {
            refundCommandService.fail(refund.getId(), failure);
        } else {
            refundCommandService.unknown(refund.getId(), failure);
        }

        return new PaymentRefundResult(status, failure);
    }

    public List<RefundDTO> getPendingRefundRequest() {

        // RefundStatus가 Failure, Unknown, Executing
        // nextRetryAt > now && retryCount <= MAX_RETRY_COUNT
        // 만역 Executing일 경우 충분한 시간이 지나여함

        return null;
    }
}
