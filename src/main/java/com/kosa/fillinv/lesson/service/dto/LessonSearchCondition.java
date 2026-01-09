package com.kosa.fillinv.lesson.service.dto;

import com.kosa.fillinv.lesson.entity.LessonType;

public record LessonSearchCondition(
        String keyword,
        LessonType lessonType,
        Long categoryId,
        LessonSortType sortType,
        int page,
        int size
) {
}
