package com.kosa.fillinv.domain.member.repository;

import com.kosa.fillinv.domain.member.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
}
