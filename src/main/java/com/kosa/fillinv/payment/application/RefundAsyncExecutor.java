package com.kosa.fillinv.payment.application;

import com.kosa.fillinv.payment.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefundAsyncExecutor {

    private final RefundService refundService;

    @Async
    public void execute(String refundId) {
        refundService.executeRefund(refundId);
    }

}
