package com.kosa.fillinv.lesson.repository;

import com.kosa.fillinv.lesson.entity.AvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableTimeRepository extends JpaRepository<AvailableTime, String> {
}
