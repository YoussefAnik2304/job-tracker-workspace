package com.jobtracker.backend.repository;

import com.jobtracker.backend.model.ApplicationStage;
import com.jobtracker.backend.model.JobApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobApplicationRepositoryTest {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Test
    void shouldSaveAndRetrieveJobApplication() {
        // Arrange
        JobApplication application = JobApplication.builder()
                .company("Google")
                .title("Software Engineer")
                .stage(ApplicationStage.APPLIED)
                .notes("Excited about this opportunity!")
                .appliedDate(LocalDate.now())
                .build();

        // Act
        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Assert: Verify persistent database state and ID assignment
        assertThat(savedApplication).isNotNull();
        assertThat(savedApplication.getId()).isNotNull();
        assertThat(savedApplication.getCompany()).isEqualTo("Google");
        assertThat(savedApplication.getTitle()).isEqualTo("Software Engineer");
        assertThat(savedApplication.getStage()).isEqualTo(ApplicationStage.APPLIED);
        assertThat(savedApplication.getNotes()).isEqualTo("Excited about this opportunity!");
        assertThat(savedApplication.getAppliedDate()).isEqualTo(LocalDate.now());

        // Verify direct retrieval from DB
        JobApplication retrieved = jobApplicationRepository.findById(savedApplication.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getCompany()).isEqualTo("Google");
        assertThat(retrieved.getTitle()).isEqualTo("Software Engineer");
    }

    @Test
    void shouldUpdateJobApplication() {
        // Arrange
        JobApplication application = JobApplication.builder()
                .company("Microsoft")
                .title("Backend Developer")
                .stage(ApplicationStage.APPLIED)
                .appliedDate(LocalDate.now())
                .build();
        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Act
        savedApplication.setStage(ApplicationStage.INTERVIEWING);
        savedApplication.setNotes("First round scheduled.");
        JobApplication updatedApplication = jobApplicationRepository.save(savedApplication);

        // Assert
        assertThat(updatedApplication.getStage()).isEqualTo(ApplicationStage.INTERVIEWING);
        assertThat(updatedApplication.getNotes()).isEqualTo("First round scheduled.");
        
        JobApplication retrieved = jobApplicationRepository.findById(savedApplication.getId()).orElse(null);
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getStage()).isEqualTo(ApplicationStage.INTERVIEWING);
    }

    @Test
    void shouldDeleteJobApplication() {
        // Arrange
        JobApplication application = JobApplication.builder()
                .company("Amazon")
                .title("Cloud Architect")
                .stage(ApplicationStage.APPLIED)
                .appliedDate(LocalDate.now())
                .build();
        JobApplication savedApplication = jobApplicationRepository.save(application);
        Long id = savedApplication.getId();

        // Act
        jobApplicationRepository.deleteById(id);

        // Assert
        boolean exists = jobApplicationRepository.existsById(id);
        assertThat(exists).isFalse();
    }

    @Test
    void shouldFindAllJobApplications() {
        // Arrange
        jobApplicationRepository.deleteAll(); // Ensure a clean table for the test
        
        JobApplication app1 = JobApplication.builder()
                .company("Netflix")
                .title("Data Engineer")
                .stage(ApplicationStage.APPLIED)
                .appliedDate(LocalDate.now())
                .build();
        JobApplication app2 = JobApplication.builder()
                .company("Apple")
                .title("iOS Developer")
                .stage(ApplicationStage.OFFER)
                .appliedDate(LocalDate.now())
                .build();
                
        jobApplicationRepository.save(app1);
        jobApplicationRepository.save(app2);

        // Act
        List<JobApplication> applications = jobApplicationRepository.findAll();

        // Assert
        assertThat(applications).hasSize(2);
    }

    @Test
    void shouldFindJobApplicationById() {
        // Arrange
        JobApplication application = JobApplication.builder()
                .company("Meta")
                .title("Frontend Engineer")
                .stage(ApplicationStage.APPLIED)
                .appliedDate(LocalDate.now())
                .build();
        JobApplication savedApplication = jobApplicationRepository.save(application);

        // Act
        JobApplication retrieved = jobApplicationRepository.findById(savedApplication.getId()).orElse(null);

        // Assert
        assertThat(retrieved).isNotNull();
        assertThat(retrieved.getId()).isEqualTo(savedApplication.getId());
        assertThat(retrieved.getCompany()).isEqualTo("Meta");
        assertThat(retrieved.getTitle()).isEqualTo("Frontend Engineer");
    }
}
