package com.kosa.fillinv.payment.service;

import com.kosa.fillinv.global.exception.ResourceException;
import com.kosa.fillinv.payment.controller.dto.CheckoutCommand;
import com.kosa.fillinv.payment.controller.dto.CheckoutResult;
import com.kosa.fillinv.payment.entity.Payment;
import com.kosa.fillinv.payment.repository.PaymentRepository;
import com.kosa.fillinv.schedule.entity.Schedule;
import com.kosa.fillinv.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ScheduleRepository scheduleRepository;

    /*
     * 스케쥴에 대한 Payment 객체를 생성 및 데이터베이스에 저장
     * Payment 객체를 통해서 이후 결제 과정에서 상태를 관리
     * */
    @Transactional
    public CheckoutResult checkout(CheckoutCommand command) {

        String scheduleId = command.scheduleId();

        // 결제할 스케쥴 정보 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceException.NotFound("스케쥴을 찾을 수 없습니다. scheduleId: " + scheduleId));

        Integer amount = schedule.getPrice();
        String orderName = schedule.getLessonTitle() + (schedule.getOptionName() != null ? " - " + schedule.getOptionName() : "");

        // 결제 준비
        Payment initPayment = Payment.builder()
                .id(UUID.randomUUID().toString())
                .orderId(scheduleId)
                .orderName(orderName)
                .buyerId(schedule.getMenteeId())
                .sellerId(schedule.getMentorId())
                .amount(amount)
                .build();

        paymentRepository.save(initPayment);

        return new CheckoutResult(command.scheduleId(), orderName, amount);
    }
}
