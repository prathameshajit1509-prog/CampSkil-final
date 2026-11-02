package com.campusconnect.repository;

import com.campusconnect.model.JobRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRoleRepository extends JpaRepository<JobRole, Long> {
    JobRole findByRoleTitle(String roleTitle);
}