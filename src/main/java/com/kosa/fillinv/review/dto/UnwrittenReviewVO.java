package com.kosa.fillinv.review.dto;

import java.time.LocalDate;

public record UnwrittenReviewVO(
        String scheduleId,
        String lessonName,
        String lessonId,
        String optionName,
        LocalDate reservationDate,
        String mentorNickname) {
}
