package com.kosa.fillinv.domain.payment.repository;

import com.kosa.fillinv.domain.payment.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, String> {
}
