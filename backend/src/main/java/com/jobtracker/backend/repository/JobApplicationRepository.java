package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link com.jobtracker.backend.model.JobApplication}
 * instances. Provides basic CRUD operations and pagination capabilities.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}
