package com.kosa.fillinv.schedule.dto.request;

public record ScheduleSearchRequest(
        String date, // 특정일 필터
        String title, // 제목 검색
        String status // 상태 필터 (PAYMENT_PENDING, APPROVED 등)
) {
}
