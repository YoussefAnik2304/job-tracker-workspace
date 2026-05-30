package com.jobtracker.backend.service.impl;

import com.jobtracker.backend.exception.ResourceNotFoundException;
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
    public void setup() {
        application = JobApplication.builder()
                .id(1L)
                .company("Google")
                .title("Software Engineer")
                .stage(ApplicationStage.APPLIED)
                .notes("Referral applied")
                .appliedDate(LocalDate.now())
                .build();
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
        given(jobApplicationRepository.findAll()).willReturn(List.of(application, app2));

        // Act
        List<JobApplication> applications = jobApplicationService.getAllApplications();

        // Assert
        assertThat(applications).isNotNull();
        assertThat(applications.size()).isEqualTo(2);
        verify(jobApplicationRepository).findAll();
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
    void shouldUpdateApplication() {
        // Arrange
        JobApplication updateData = JobApplication.builder()
                .company("Google")
                .title("Software Engineer")
                .stage(ApplicationStage.INTERVIEWING)
                .notes("Interview tomorrow")
                .appliedDate(LocalDate.now())
                .build();
                
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
