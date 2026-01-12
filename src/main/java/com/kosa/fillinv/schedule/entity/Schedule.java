package com.kosa.fillinv.schedule.entity;

import com.kosa.fillinv.lesson.entity.Lesson;
import com.kosa.fillinv.lesson.entity.Option;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Entity
@Getter
@ToString
@Table(name = "schedules")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @Column(name = "schedule_id", nullable = false)
    private String id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status;

    @Column(name = "request_content")
    private String requestContent;

    /* ===== Lesson Snapshot ===== */
    // 레슨 하나 당 없거나 한 개 또는 여러 개의 스케쥴 존재
    @Column(name = "lesson_title", nullable = false)
    private String lessonTitle;

    @Column(name = "lesson_type", nullable = false)
    private String lessonType;

    @Column(name = "lesson_description", nullable = false)
    private String lessonDescription;

    @Column(name = "lesson_location", nullable = false)
    private String lessonLocation;

    @Column(name = "lesson_category_name", nullable = false)
    private String lessonCategoryName;

    @Column(name = "lesson_mentor_id", nullable = false)
    private String mentor;

    /* ===== Option Snapshot ===== */
    // 옵션 하나 당 없거나 한 개 또는 여러 개의 스케쥴 존재
    @Column(name = "option_name", nullable = false)
    private String optionName;

    @Column(name = "option_minute", nullable = false)
    private Integer optionMinute;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "lesson_id")
    private String lessonId;

    @Column(name = "mentee_id")
    private String mentee;

    @Column(name = "option_id")
    private String optionId;

    // 스케쥴 생성 메서드
    public static Schedule create(Lesson lesson, Option option, Instant startTime, String memberId) {

        // 시작 시간 + 옵션 분(minutes) =  종료 시간
        Instant endTime = startTime.plus(option.getMinute(), ChronoUnit.MINUTES);

        return Schedule.builder() // 빌더 코드 숨김
                .id("1")
                .mentor(memberId) // 멘토 id 저장
                .lessonId(lesson.getId())
                .lessonLocation(lesson.getLocation())
                .optionName(option.getName())
                .optionMinute(option.getMinute())

                // 시간 정보 - 한국 표준시 적용
                .date(startTime.atZone(java.time.ZoneId.of("Asia/Seoul")).toLocalDate())
                .startTime(startTime.atZone(java.time.ZoneId.of("Asia/Seoul")).toLocalTime())
                .endTime(endTime.atZone(java.time.ZoneId.of("Asia/Seoul")).toLocalTime())

                .status(ScheduleStatus.PAYMENT_PENDING) // 초기 상태 설정 (결제 대기)
                .build();
    }
}
