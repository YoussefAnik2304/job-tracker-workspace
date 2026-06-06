package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.service.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.ApplicationStage;
import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.repository.JobApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceImplTest {

	@Mock
	private JobApplicationRepository jobApplicationRepository;

	@InjectMocks
	private JobApplicationServiceImpl jobApplicationService;

	private JobApplication application;

	@BeforeEach
	void setup() {
		application = JobApplication.builder().id(1L).company("Google").title("Software Engineer")
				.stage(ApplicationStage.APPLIED).notes("Referral applied").appliedDate(LocalDate.now()).build();
	}

	@Test
	void shouldSaveApplication() {
		// Arrange
		given(jobApplicationRepository.save(application)).willReturn(application);

		// Act
		JobApplication savedApplication = jobApplicationService.saveApplication(application);

		// Assert
		assertThat(savedApplication).isNotNull();
		assertThat(savedApplication.getId()).isEqualTo(1L);
		verify(jobApplicationRepository).save(application);
	}

	@Test
	void shouldGetAllApplications() {
		// Arrange
		JobApplication app2 = JobApplication.builder().id(2L).company("Amazon").build();
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<JobApplication> page = new PageImpl<>(List.of(application, app2));
		given(jobApplicationRepository.findAll(pageRequest)).willReturn(page);

		// Act
		Page<JobApplication> applications = jobApplicationService.getAllApplications(pageRequest);

		// Assert
		assertThat(applications).isNotNull();
		assertThat(applications.getContent()).hasSize(2);
		verify(jobApplicationRepository).findAll(pageRequest);
	}

	@Test
	void shouldGetApplicationById() {
		// Arrange
		given(jobApplicationRepository.findById(1L)).willReturn(Optional.of(application));

		// Act
		JobApplication retrieved = jobApplicationService.getApplicationById(1L);

		// Assert
		assertThat(retrieved).isNotNull();
		assertThat(retrieved.getCompany()).isEqualTo("Google");
		verify(jobApplicationRepository).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenGetApplicationByIdNotFound() {
		// Arrange
		given(jobApplicationRepository.findById(1L)).willReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> jobApplicationService.getApplicationById(1L));
		verify(jobApplicationRepository).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenUpdatingRejectedApplication() {
		// Arrange
		JobApplication rejectedApplication = JobApplication.builder().id(1L).stage(ApplicationStage.REJECTED).build();
		given(jobApplicationRepository.findById(1L)).willReturn(Optional.of(rejectedApplication));

		JobApplication updateData = JobApplication.builder().stage(ApplicationStage.INTERVIEWING).build();

		// Act & Assert
		assertThrows(com.jobtracker.backend.service.exception.InvalidBusinessRuleException.class,
				() -> jobApplicationService.updateApplication(1L, updateData));
		verify(jobApplicationRepository, never()).save(any(JobApplication.class));
	}

	@Test
	void shouldUpdateApplication() {
		// Arrange
		JobApplication updateData = JobApplication.builder().company("Google").title("Software Engineer")
				.stage(ApplicationStage.INTERVIEWING).notes("Interview tomorrow").appliedDate(LocalDate.now()).build();

		given(jobApplicationRepository.findById(1L)).willReturn(Optional.of(application));
		given(jobApplicationRepository.save(any(JobApplication.class))).willReturn(application);

		// Act
		JobApplication updated = jobApplicationService.updateApplication(1L, updateData);

		// Assert
		assertThat(updated.getStage()).isEqualTo(ApplicationStage.INTERVIEWING);
		assertThat(updated.getNotes()).isEqualTo("Interview tomorrow");
		verify(jobApplicationRepository).save(any(JobApplication.class));
	}

	@Test
	void shouldDeleteApplication() {
		// Arrange
		given(jobApplicationRepository.findById(1L)).willReturn(Optional.of(application));

		// Act
		jobApplicationService.deleteApplication(1L);

		// Assert
		verify(jobApplicationRepository).delete(application);
	}
}
