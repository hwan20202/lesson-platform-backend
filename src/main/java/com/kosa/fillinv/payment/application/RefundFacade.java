package com.kosa.fillinv.payment.application;

import com.kosa.fillinv.payment.executor.RefundAsyncExecutor;
import com.kosa.fillinv.payment.service.RefundCommandService;
import com.kosa.fillinv.payment.service.dto.PaymentRefundCommand;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundFacade {

    private final RefundCommandService refundCommandService;
    private final RefundAsyncExecutor refundAsyncExecutor;

    public RefundDTO requestRefund(PaymentRefundCommand command) {

        RefundDTO refund = refundCommandService.createRefund(PaymentRefundCommand.toRefundCreateCommand(command));

        refundAsyncExecutor.execute(refund.refundId());

        return refund;
    }
}
