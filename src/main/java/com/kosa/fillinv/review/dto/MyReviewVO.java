package com.kosa.fillinv.review.dto;

import com.kosa.fillinv.review.entity.Review;
import java.time.LocalDate;

public record MyReviewVO(
        Review review,
        String lessonName,
        String optionName,
        LocalDate reservationDate,
        String mentorNickname) {
}
