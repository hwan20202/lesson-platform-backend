package com.kosa.fillinv.domain.lesson.repository;

import com.kosa.fillinv.domain.lesson.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<Option, String> {
}
