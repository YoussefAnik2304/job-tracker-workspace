package com.jobtracker.backend.service;

import com.jobtracker.backend.model.JobApplication;
import java.util.List;

public interface JobApplicationService {
    JobApplication saveApplication(JobApplication application);
    List<JobApplication> getAllApplications();
    JobApplication getApplicationById(Long id);
    JobApplication updateApplication(Long id, JobApplication applicationDetails);
    void deleteApplication(Long id);
}
