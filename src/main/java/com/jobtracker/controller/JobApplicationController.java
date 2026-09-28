package com.jobtracker.controller;

import com.jobtracker.dto.application.JobApplicationRequest;
import com.jobtracker.dto.application.JobApplicationResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.User;
import com.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    /**
     * @AuthenticationPrincipal injects the User populated by
     * JwtAuthenticationFilter for this request - its id is the only source
     * of "current user" ever used to scope queries below.
     */
    @GetMapping
    public ResponseEntity<Page<JobApplicationResponse>> list(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) ApplicationStatus status,
            Pageable pageable
    ) {
        return ResponseEntity.ok(jobApplicationService.list(currentUser.getId(), status, pageable));
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        JobApplicationResponse created = jobApplicationService.create(currentUser.getId(), currentUser, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> getById(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(jobApplicationService.getById(currentUser.getId(), id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobApplicationResponse> update(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationRequest request
    ) {
        return ResponseEntity.ok(jobApplicationService.update(currentUser.getId(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id
    ) {
        jobApplicationService.delete(currentUser.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
