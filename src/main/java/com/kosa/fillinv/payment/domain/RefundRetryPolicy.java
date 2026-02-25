package com.kosa.fillinv.payment.domain;

import com.kosa.fillinv.payment.entity.Refund;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
public class RefundRetryPolicy {

    private static final int MAX_RETRY_COUNT = 3;
    private static final Duration EXECUTING_TIMEOUT = Duration.ofMinutes(5);

//    public boolean canRetry(Refund refund, Instant now) {
//        if (refund.getRetryCount() >= MAX_RETRY_COUNT) {
//            return false;
//        }
//
//        if (refund.getNextAttemptAt().isAfter(now)) {
//            return false;
//        }
//
//        if (refund.isExecuting() &&
//                refund.getLastRetryAt().isAfter(now.minus(EXECUTING_TIMEOUT))) {
//            return false;
//        }
//
//        return true;
//    }

    public Instant calculateNextAttemptAt(Refund refund, Instant now) {
        long backoffMinutes = (long) Math.pow(2, refund.getRetryCount());
        return now.plus(Duration.ofMinutes(backoffMinutes));
    }
}
