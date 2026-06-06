package com.jobtracker.backend.dto;

import com.jobtracker.backend.model.ApplicationStage;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for JobApplication. This class is used to securely
 * transfer data between the client (Angular frontend) and the server, ensuring
 * that sensitive database fields are not accidentally exposed.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

	/** Unique identifier for the job application */
	private Long id;

	/** Name of the company applying to */
	@NotBlank(message = "Company name is required")
	private String company;

	/** Title of the job position */
	@NotBlank(message = "Job title is required")
	private String title;

	/** Current stage of the application process */
	private ApplicationStage stage;

	/** Optional notes about the application */
	private String notes;

	/** Date the application was submitted */
	private LocalDate appliedDate;

	/** Timestamp when the record was created */
	private LocalDateTime createdAt;

	/** Timestamp when the record was last updated */
	private LocalDateTime updatedAt;
}
