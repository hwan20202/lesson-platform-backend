package com.kosa.fillinv.domain.lesson.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @Column(name = "option_id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "minute", nullable = false)
    private Integer minute;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;
}
