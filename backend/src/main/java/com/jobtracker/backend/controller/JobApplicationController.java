package com.jobtracker.backend.controller;

import com.jobtracker.backend.dto.JobApplicationDTO;
import com.jobtracker.backend.mapper.JobApplicationMapper;
import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing Job Applications. This API layer is responsible
 * for receiving HTTP requests from the Angular frontend, mapping DTOs to
 * internal entities, and returning standard HTTP responses.
 */
@Slf4j
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200")
public class JobApplicationController {

	private final JobApplicationService jobApplicationService;
	private final JobApplicationMapper jobApplicationMapper;

	/**
	 * Constructor injection for required dependencies.
	 *
	 * @param jobApplicationService
	 *            the service handling business logic
	 * @param jobApplicationMapper
	 *            the mapper converting between DTOs and Entities
	 */
	public JobApplicationController(JobApplicationService jobApplicationService,
			JobApplicationMapper jobApplicationMapper) {
		this.jobApplicationService = jobApplicationService;
		this.jobApplicationMapper = jobApplicationMapper;
	}

	/**
	 * Creates a new job application.
	 *
	 * @param applicationDto
	 *            the application data from the client
	 * @return the created JobApplicationDTO with its assigned ID
	 */
	@PostMapping
	public ResponseEntity<JobApplicationDTO> createApplication(@Valid @RequestBody JobApplicationDTO applicationDto) {
		log.info("Received request to create application for company: {}", applicationDto.getCompany());
		JobApplication entity = jobApplicationMapper.toEntity(applicationDto);
		JobApplication savedEntity = jobApplicationService.saveApplication(entity);
		return new ResponseEntity<>(jobApplicationMapper.toDto(savedEntity), HttpStatus.CREATED);
	}

	/**
	 * Retrieves a paginated list of all job applications.
	 *
	 * @param pageable
	 *            pagination and sorting configuration
	 * @return a Page containing JobApplicationDTOs
	 */
	@GetMapping
	public ResponseEntity<Page<JobApplicationDTO>> getAllApplications(Pageable pageable) {
		log.info("Received request to fetch all applications with pagination: {}", pageable);
		Page<JobApplication> applications = jobApplicationService.getAllApplications(pageable);
		Page<JobApplicationDTO> dtoPage = applications.map(jobApplicationMapper::toDto);
		return new ResponseEntity<>(dtoPage, HttpStatus.OK);
	}

	/**
	 * Retrieves a specific job application by its ID.
	 *
	 * @param id
	 *            the unique identifier of the job application
	 * @return the requested JobApplicationDTO
	 */
	@GetMapping("/{id}")
	public ResponseEntity<JobApplicationDTO> getApplicationById(@PathVariable Long id) {
		log.info("Received request to fetch application with id: {}", id);
		JobApplication application = jobApplicationService.getApplicationById(id);
		return new ResponseEntity<>(jobApplicationMapper.toDto(application), HttpStatus.OK);
	}

	/**
	 * Updates an existing job application.
	 *
	 * @param id
	 *            the unique identifier of the application to update
	 * @param applicationDetails
	 *            the updated application data
	 * @return the fully updated JobApplicationDTO
	 */
	@PutMapping("/{id}")
	public ResponseEntity<JobApplicationDTO> updateApplication(@PathVariable Long id,
			@Valid @RequestBody JobApplicationDTO applicationDetails) {
		log.info("Received request to update application with id: {}", id);
		JobApplication entityToUpdate = jobApplicationMapper.toEntity(applicationDetails);
		JobApplication updatedEntity = jobApplicationService.updateApplication(id, entityToUpdate);
		return new ResponseEntity<>(jobApplicationMapper.toDto(updatedEntity), HttpStatus.OK);
	}

	/**
	 * Deletes a specific job application.
	 *
	 * @param id
	 *            the unique identifier of the application to delete
	 * @return a 204 No Content response
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
		log.info("Received request to delete application with id: {}", id);
		jobApplicationService.deleteApplication(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
