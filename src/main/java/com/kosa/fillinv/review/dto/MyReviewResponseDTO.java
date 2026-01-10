package com.kosa.fillinv.review.dto;

import com.kosa.fillinv.review.entity.Review;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class MyReviewResponseDTO {
    private String reviewId;
    private Integer score;
    private String content;
    private String lessonId;
    private String scheduleId;
    private String lessonName;
    private Instant createdAt;

    public static MyReviewResponseDTO from(ReviewWithScheduleLessonNameVO vo) {
        Review r = vo.getReview();
        return MyReviewResponseDTO.builder()
                .reviewId(r.getId())
                .score(r.getScore())
                .content(r.getContent())
                .lessonId(r.getLessonId())
                .scheduleId(r.getScheduleId())
                .lessonName(vo.getLessonName())
                .createdAt(r.getCreatedAt())
                .build();
    }
}
