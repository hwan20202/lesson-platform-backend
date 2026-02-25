package com.kosa.fillinv.payment.repository;

import com.kosa.fillinv.payment.entity.Refund;
import com.kosa.fillinv.payment.entity.RefundStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {

    @Query("""
                SELECT r
                FROM Refund r
                WHERE
                    (
                        r.refundStatus IN ('FAILURE', 'UNKNOWN')
                        OR
                        (r.refundStatus = 'EXECUTING' AND r.lastRequestedAt < :timeoutThreshold)
                    )
                AND r.retryCount <= :maxRetryCount
                AND r.nextRetryAt >= :now
                ORDER BY r.nextRetryAt ASC
            """)
    List<Refund> findPendingRefundRequests(
            @Param("timeoutThreshold") Instant timeoutThreshold,
            @Param("maxRetryCount") int maxRetryCount,
            @Param("now") Instant now
    );
}
