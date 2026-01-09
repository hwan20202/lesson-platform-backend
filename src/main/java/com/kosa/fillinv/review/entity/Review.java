package com.kosa.fillinv.review.entity;

import com.kosa.fillinv.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;

@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @Getter
    @Column(name = "review_id")
    private String id;

    @Getter
    @Column(name = "score", nullable = false)
    private Integer score;

    @Getter
    @Column(name = "content", nullable = false)
    private String content;

    @Getter
    @Column(name = "writer_id", nullable = false)
    private String writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", insertable = false, updatable = false)
    private Member writer;

    @Getter
    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Getter
    @Column(name = "schedule_id", nullable = false)
    private String scheduleId;

    @Getter
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Getter
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Getter
    @Column(name = "deleted_at")
    private Instant deletedAt;
}