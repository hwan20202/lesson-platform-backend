package com.kosa.fillinv.payment.repository;

import com.kosa.fillinv.payment.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, String> {
}
