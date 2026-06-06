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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {

	private final JobApplicationRepository jobApplicationRepository;

	public JobApplicationServiceImpl(JobApplicationRepository jobApplicationRepository) {
		this.jobApplicationRepository = jobApplicationRepository;
	}

	@Override
	public JobApplication saveApplication(JobApplication application) {
		log.info("Saving new application for company: {}", application.getCompany());
		return jobApplicationRepository.save(application);
	}

	@Override
	public Page<JobApplication> getAllApplications(Pageable pageable) {
		log.debug("Fetching applications from database. Page number: {}, Page size: {}", pageable.getPageNumber(),
				pageable.getPageSize());
		return jobApplicationRepository.findAll(pageable);
	}

	@Override
	public JobApplication getApplicationById(Long id) {
		log.debug("Fetching application from database with id: {}", id);
		return jobApplicationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("JobApplication not found with id: " + id));
	}

	@Override
	public JobApplication updateApplication(Long id, JobApplication applicationDetails) {
		JobApplication existingApplication = getApplicationById(id);

		if (existingApplication.getStage() == ApplicationStage.REJECTED) {
			log.warn("Attempted to update an application (id={}) that is already in REJECTED stage", id);
			throw new InvalidBusinessRuleException(
					"Cannot update an application that is already in the REJECTED stage.");
		}

		log.info("Updating application (id={}) details for company: {}", id, applicationDetails.getCompany());
		existingApplication.setCompany(applicationDetails.getCompany());
		existingApplication.setTitle(applicationDetails.getTitle());
		existingApplication.setStage(applicationDetails.getStage());
		existingApplication.setNotes(applicationDetails.getNotes());
		existingApplication.setAppliedDate(applicationDetails.getAppliedDate());

		return jobApplicationRepository.save(existingApplication);
	}

	@Override
	public void deleteApplication(Long id) {
		log.info("Deleting application with id: {}", id);
		JobApplication existingApplication = getApplicationById(id);
		jobApplicationRepository.delete(existingApplication);
	}
}
