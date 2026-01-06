package com.kosa.fillinv.domain.lesson.repository;

import com.kosa.fillinv.domain.lesson.entity.AvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableTimeRepository extends JpaRepository<AvailableTime, String> {
}
