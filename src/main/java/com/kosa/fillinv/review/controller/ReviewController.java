package com.kosa.fillinv.review.controller;

import com.kosa.fillinv.review.dto.LessonReviewListResponseDTO;
import com.kosa.fillinv.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/lessons/{lessonId}/reviews")
    public ResponseEntity<LessonReviewListResponseDTO> getLessonReviewList(
            @PathVariable("lessonId") String lessonId,
            @PageableDefault(size = 20) Pageable pageable) {
        if (pageable.getPageSize() > 100) {
            pageable = PageRequest.of(pageable.getPageNumber(), 100, pageable.getSort());
        }
        LessonReviewListResponseDTO reviewList = reviewService.getReviewListByLesson(lessonId, pageable);
        return ResponseEntity.ok(reviewList);
    }
}
