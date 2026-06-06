package com.jobtracker.backend.service;

import com.jobtracker.backend.model.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface defining the business operations for Job Applications.
 */
public interface JobApplicationService {

	/**
	 * Saves a new job application to the database.
	 *
	 * @param application
	 *            the application to save
	 * @return the saved application with an assigned ID
	 */
	JobApplication saveApplication(JobApplication application);

	/**
	 * Retrieves a paginated list of all job applications.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return a page of job applications
	 */
	Page<JobApplication> getAllApplications(Pageable pageable);

	/**
	 * Retrieves a specific job application by its ID.
	 *
	 * @param id
	 *            the unique identifier
	 * @return the found job application
	 * @throws com.jobtracker.backend.service.exception.ResourceNotFoundException
	 *             if not found
	 */
	JobApplication getApplicationById(Long id);

	/**
	 * Updates an existing job application.
	 *
	 * @param id
	 *            the unique identifier of the application to update
	 * @param applicationDetails
	 *            the updated application details
	 * @return the updated job application
	 * @throws com.jobtracker.backend.service.exception.InvalidBusinessRuleException
	 *             if rules are violated
	 */
	JobApplication updateApplication(Long id, JobApplication applicationDetails);

	/**
	 * Deletes a job application by its ID.
	 *
	 * @param id
	 *            the unique identifier
	 * @throws com.jobtracker.backend.service.exception.ResourceNotFoundException
	 *             if not found
	 */
	void deleteApplication(Long id);
}
