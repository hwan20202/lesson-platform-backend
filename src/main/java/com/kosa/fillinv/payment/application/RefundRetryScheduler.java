package com.kosa.fillinv.payment.application;

import com.kosa.fillinv.payment.service.RefundService;
import com.kosa.fillinv.payment.service.dto.RefundDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RefundRetryScheduler {

    private final RefundService refundService;
    private final RefundAsyncExecutor refundAsyncExecutor;

    @Scheduled(
            fixedDelay = 10,
            initialDelay = 5,
            timeUnit = TimeUnit.SECONDS,
            scheduler = "refundRetryExecutor"
    )
    public void schedule() {
        List<RefundDTO> pendingRefnudList = refundService.getPendingRefundRequest();

        for (RefundDTO dto : pendingRefnudList) {
            refundAsyncExecutor.execute(dto.refundId());
        }
    }
}
