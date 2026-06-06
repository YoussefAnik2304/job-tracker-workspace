package com.jobtracker.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobtracker.backend.service.exception.ResourceNotFoundException;
import com.jobtracker.backend.model.ApplicationStage;
import com.jobtracker.backend.model.JobApplication;
import com.jobtracker.backend.mapper.JobApplicationMapper;
import com.jobtracker.backend.service.JobApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobApplicationController.class)
@org.springframework.context.annotation.Import(JobApplicationMapper.class)
class JobApplicationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private JobApplicationService jobApplicationService;

	private ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

	private JobApplication application;

	@BeforeEach
	void setUp() {
		application = JobApplication.builder().id(1L).company("Google").title("Software Engineer")
				.stage(ApplicationStage.APPLIED).notes("Referral applied").appliedDate(LocalDate.now()).build();
	}

	@Test
	void shouldCreateApplication() throws Exception {
		given(jobApplicationService.saveApplication(any(JobApplication.class))).willReturn(application);

		mockMvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(application))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(application.getId()))
				.andExpect(jsonPath("$.company").value(application.getCompany()));
	}

	@Test
	void shouldGetAllApplications() throws Exception {
		Page<JobApplication> page = new PageImpl<>(List.of(application));
		given(jobApplicationService.getAllApplications(any(Pageable.class))).willReturn(page);

		mockMvc.perform(get("/api/applications")).andExpect(status().isOk())
				.andExpect(jsonPath("$.content.size()").value(1))
				.andExpect(jsonPath("$.content[0].company").value(application.getCompany()));
	}

	@Test
	void shouldGetApplicationById() throws Exception {
		given(jobApplicationService.getApplicationById(1L)).willReturn(application);

		mockMvc.perform(get("/api/applications/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.company").value(application.getCompany()));
	}

	@Test
	void shouldReturn404WhenGetApplicationByIdNotFound() throws Exception {
		given(jobApplicationService.getApplicationById(99L))
				.willThrow(new ResourceNotFoundException("JobApplication not found"));

		mockMvc.perform(get("/api/applications/99")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturn422WhenUpdatingRejectedApplication() throws Exception {
		given(jobApplicationService.updateApplication(eq(1L), any(JobApplication.class)))
				.willThrow(new com.jobtracker.backend.service.exception.InvalidBusinessRuleException(
						"Cannot update REJECTED application"));

		mockMvc.perform(put("/api/applications/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(application))).andExpect(status().isUnprocessableContent())
				.andExpect(jsonPath("$.error").value("Unprocessable Entity"));
	}

	@Test
	void shouldUpdateApplication() throws Exception {
		given(jobApplicationService.updateApplication(eq(1L), any(JobApplication.class))).willReturn(application);

		mockMvc.perform(put("/api/applications/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(application))).andExpect(status().isOk())
				.andExpect(jsonPath("$.company").value(application.getCompany()));
	}

	@Test
	void shouldDeleteApplication() throws Exception {
		doNothing().when(jobApplicationService).deleteApplication(1L);

		mockMvc.perform(delete("/api/applications/1")).andExpect(status().isNoContent());
	}

	@Test
	void shouldReturn404WhenDeleteApplicationNotFound() throws Exception {
		doThrow(new ResourceNotFoundException("Not found")).when(jobApplicationService).deleteApplication(99L);

		mockMvc.perform(delete("/api/applications/99")).andExpect(status().isNotFound());
	}

	@Test
	void shouldReturn400WhenCreateApplicationWithInvalidData() throws Exception {
		JobApplication invalidApplication = JobApplication.builder().company("") // Blank company, should fail @NotBlank
				.title("Software Engineer").stage(ApplicationStage.APPLIED).build();

		mockMvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(invalidApplication))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.company").value("Company name is required"));
	}
}
