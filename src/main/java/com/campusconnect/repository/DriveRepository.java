package com.campusconnect.repository;

import com.campusconnect.model.OnCampusDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriveRepository extends JpaRepository<OnCampusDrive, Long> {
}