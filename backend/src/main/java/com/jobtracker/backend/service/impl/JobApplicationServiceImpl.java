package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.service.exception.ResourceNotFoundException;
import com.jobtracker.backend.service.exception.InvalidBusinessRuleException;
import com.jobtracker.backend.model.ApplicationStage;
import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.repository.JobApplicationRepository;
import com.jobtracker.backend.service.JobApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    @Override
    public JobApplication saveApplication(JobApplication application) {
        return jobApplicationRepository.save(application);
    }

    @Override
    public Page<JobApplication> getAllApplications(Pageable pageable) {
        return jobApplicationRepository.findAll(pageable);
    }

    @Override
    public JobApplication getApplicationById(Long id) {
        return jobApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
    }

    @Override
    public JobApplication updateApplication(Long id, JobApplication applicationDetails) {
        JobApplication existingApplication = getApplicationById(id);
        
        if (existingApplication.getStage() == ApplicationStage.REJECTED) {
            throw new InvalidBusinessRuleException("Cannot update an application that is already in the REJECTED stage.");
        }
        
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
