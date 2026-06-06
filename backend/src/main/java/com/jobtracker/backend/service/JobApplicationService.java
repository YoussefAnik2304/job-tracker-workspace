package com.jobtracker.backend.service;

import com.jobtracker.backend.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobApplicationService {
	JobApplication saveApplication(JobApplication application);
	Page<JobApplication> getAllApplications(Pageable pageable);
	JobApplication getApplicationById(Long id);
	JobApplication updateApplication(Long id, JobApplication applicationDetails);
	void deleteApplication(Long id);
}
