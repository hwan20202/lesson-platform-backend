package com.kosa.fillinv.schedule.entity;

import com.kosa.fillinv.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    @Id
    @Column(name = "schedule_id", nullable = false)
    private String id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ScheduleStatus status;

    @Column(name = "request_content")
    private String requestContent;

    /* ===== Lesson Snapshot ===== */
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

    @Column(name = "mentor_nickname", nullable = false)
    private String mentorNickname;

    /* ===== Option Snapshot ===== */
    // 옵션 하나 당 없거나 한 개 또는 여러 개의 스케쥴 존재
    @Column(name = "option_name")
    private String optionName;

    @Column(name = "option_minute")
    private Integer optionMinute;

    @Column(name = "price")
    private Integer price;

    /* 외부 테이블 키 */
    @Column(name = "lesson_id", nullable = false)
    private String lessonId;

    @Column(name = "mentee_id", nullable = false)
    private String menteeId;

    @Column(name = "lesson_mentor_id", nullable = false)
    private String mentorId;

    @Column(name = "option_id")
    private String optionId;

    @Column(name = "available_time_id")
    private String availableTimeId;


    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
    private List<ScheduleTime> scheduleTimeList = new ArrayList<>();

    public void addScheduleTime(ScheduleTime scheduleTime) {
        scheduleTime.setSchedule(this);
        this.scheduleTimeList.add(scheduleTime);
    }

    public void addScheduleTime(List<ScheduleTime> scheduleTimeList) {
        scheduleTimeList.forEach(this::addScheduleTime);
    }
}
