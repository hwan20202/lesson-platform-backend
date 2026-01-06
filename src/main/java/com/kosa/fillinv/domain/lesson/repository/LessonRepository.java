package com.kosa.fillinv.domain.lesson.repository;

import com.kosa.fillinv.domain.lesson.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, String> {
}
