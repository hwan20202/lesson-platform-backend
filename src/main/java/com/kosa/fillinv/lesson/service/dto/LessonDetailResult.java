package com.kosa.fillinv.lesson.service.dto;

import com.kosa.fillinv.lesson.service.client.MentorSummaryDTO;

import java.time.Instant;
import java.util.List;

public record LessonDetailResult(
        Mentor mentor,
        Lesson lesson,
        List<AvailableTime> availableTimes
) {
    public static LessonDetailResult of(MentorSummaryDTO mentorSummaryDTO, LessonDTO lessonDTO) {
        return new LessonDetailResult(
                Mentor.of(mentorSummaryDTO),
                Lesson.of(lessonDTO),
                lessonDTO.availableTimeDTOList().stream().map(AvailableTime::of).toList()
        );
    }

    public record Mentor(
            String mentorId,
            String nickname,
            String profileImage,
            String introduction
    ) {
        public static Mentor of(MentorSummaryDTO mentorSummaryDTO) {
            return new Mentor(
                    mentorSummaryDTO.mentorId(),
                    mentorSummaryDTO.nickname(),
                    mentorSummaryDTO.profileImage(),
                    mentorSummaryDTO.introduction()
            );
        }
    }

    public record Lesson(
            String lessonId,
            String description
    ) {
        public static Lesson of(LessonDTO lessonDTO) {
            return new Lesson(
                    lessonDTO.id(),
                    lessonDTO.description()
            );
        }
    }

    public record AvailableTime(
            String availableTimeId,
            Instant startTime,
            Instant endTime,
            Integer price
    ) {
        public static AvailableTime of(AvailableTimeDTO availableTimeDTO) {
            return new AvailableTime(
                    availableTimeDTO.id(),
                    availableTimeDTO.startTime(),
                    availableTimeDTO.endTime(),
                    availableTimeDTO.price()
            );
        }
    }
}
