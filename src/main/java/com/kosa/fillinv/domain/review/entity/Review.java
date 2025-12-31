package com.kosa.fillinv.domain.review.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Column(name = "review_id", nullable = false)
    private String id;

    @Column(name = "score", nullable = false)
    private Integer score;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "writer_id", nullable = false)
    private String writer_id;

    @Column(name = "lesson_id", nullable = false)
    private String lesson_id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
