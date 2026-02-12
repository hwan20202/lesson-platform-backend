package com.kosa.fillinv.payment.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("local")
class RefundServiceTest {

    @Test
    @DisplayName("결제 취소 요청 시 Refund 객체가 생성된다.")
    void createRefund_whenPaymentCancelRequested() {

    }

    @Test
    @DisplayName("결제 취소 api 호출 직전에 Refund 객체 상태가 EXECUTING으로 변경된다.")
    void updateStatusToExecuting_beforeCallTossPaymentCancelApi() {

    }

    @Test
    @DisplayName("결제 취소 api가 성공하면 Refund 객체 상태가 SUCCESS로 변경된다.")
    void updateStatusToSuccess_afterTossPaymentCancelApiSuccess() {

    }

    @Test
    @DisplayName("결제 취소 api가 실패하면 Refund 객체 상태가 Failure로 변경된다.")
    void updateStatusToFailure_afterTossPaymentCancelApiFail() {

    }

    @Test
    @DisplayName("결제 취소 api 결과를 알 수 없으면 Refund 객체 상태가 Unknown으로 변경된다.")
    void updateStatusToUnknown_afterTossPaymentCancelApiUnknownResult() {

    }

    @Test
    @DisplayName("Refund 객체의 상태가 업데이트되면 RefundHistory가 생성된다.")
    void createRefundHistory_whenRefundStatusUpdated() {

    }

    @Test
    @DisplayName("결제 취소가 성공하면 스케쥴의 상태가 취소로 변경된다")
    void updateScheduleStatusToCanceled_whenRefundSuccess() {

    }

}