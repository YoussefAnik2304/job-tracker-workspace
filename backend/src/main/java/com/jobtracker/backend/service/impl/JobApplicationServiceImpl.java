package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.repository.JobApplicationRepository;
import com.jobtracker.backend.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    @Autowired
    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public JobApplication saveApplication(JobApplication application) {
        return jobApplicationRepository.save(application);
    }

    @Override
    public List<JobApplication> getAllApplications() {
        return jobApplicationRepository.findAll();
    }

    @Override
    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
    }

    @Override
    public JobApplication updateApplication(Long id, JobApplication applicationDetails) {
        JobApplication existingApplication = getApplicationById(id);
        
        existingApplication.setCompany(applicationDetails.getCompany());
        existingApplication.setTitle(applicationDetails.getTitle());
        existingApplication.setStage(applicationDetails.getStage());
        existingApplication.setNotes(applicationDetails.getNotes());
        existingApplication.setAppliedDate(applicationDetails.getAppliedDate());
        
        return jobApplicationRepository.save(existingApplication);
    }

    @Override
    public void deleteApplication(Long id) {
        JobApplication existingApplication = getApplicationById(id);
        jobApplicationRepository.delete(existingApplication);
    }
}
