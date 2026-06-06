package com.jobtracker.backend.controller;

import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.service.JobApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*") // Allows requests from the React frontend
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    // POST /api/applications
    @PostMapping
    public ResponseEntity<JobApplication> createApplication(@Valid @RequestBody JobApplication application) {
        JobApplication savedApplication = jobApplicationService.saveApplication(application);
        return new ResponseEntity<>(savedApplication, HttpStatus.CREATED);
    }

    // GET /api/applications
    @GetMapping
    public ResponseEntity<Page<JobApplication>> getAllApplications(Pageable pageable) {
        Page<JobApplication> applications = jobApplicationService.getAllApplications(pageable);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    // GET /api/applications/{id}
    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getApplicationById(@PathVariable Long id) {
        JobApplication application = jobApplicationService.getApplicationById(id);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    // PUT /api/applications/{id}
    @PutMapping("/{id}")
    public ResponseEntity<JobApplication> updateApplication(@PathVariable Long id, @Valid @RequestBody JobApplication applicationDetails) {
        JobApplication updatedApplication = jobApplicationService.updateApplication(id, applicationDetails);
        return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
    }

    // DELETE /api/applications/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        jobApplicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
