package com.jobtracker.backend.mapper;

import com.jobtracker.backend.dto.JobApplicationDTO;
import com.jobtracker.backend.model.JobApplication;
import org.springframework.stereotype.Component;

/**
 * Mapper utility class to convert between JobApplication Entity and
 * JobApplicationDTO. This prevents the Service and Controller layers from
 * leaking database models.
 */
@Component
public class JobApplicationMapper {

	/**
	 * Converts a JobApplication Entity into a JobApplicationDTO.
	 *
	 * @param entity
	 *            the JobApplication database entity
	 * @return the mapped JobApplicationDTO
	 */
	public JobApplicationDTO toDto(JobApplication entity) {
		if (entity == null) {
			return null;
		}

		return JobApplicationDTO.builder().id(entity.getId()).company(entity.getCompany()).title(entity.getTitle())
				.stage(entity.getStage()).notes(entity.getNotes()).appliedDate(entity.getAppliedDate())
				.createdAt(entity.getCreatedAt()).updatedAt(entity.getUpdatedAt()).build();
	}

	/**
	 * Converts a JobApplicationDTO into a JobApplication Entity.
	 *
	 * @param dto
	 *            the JobApplicationDTO from the client
	 * @return the mapped JobApplication database entity
	 */
	public JobApplication toEntity(JobApplicationDTO dto) {
		if (dto == null) {
			return null;
		}

		return JobApplication.builder().id(dto.getId()).company(dto.getCompany()).title(dto.getTitle())
				.stage(dto.getStage()).notes(dto.getNotes()).appliedDate(dto.getAppliedDate())
				.createdAt(dto.getCreatedAt()).updatedAt(dto.getUpdatedAt()).build();
	}
}
