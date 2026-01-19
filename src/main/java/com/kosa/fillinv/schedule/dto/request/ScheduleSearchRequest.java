package com.kosa.fillinv.schedule.dto.request;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record ScheduleSearchRequest(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date, // 특정일 필터
        String title, // 제목 검색
        String status // 상태 필터 (PAYMENT_PENDING, APPROVED 등)
) {
}
