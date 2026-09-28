package com.jobtracker.controller;

import com.jobtracker.dto.note.ApplicationNoteRequest;
import com.jobtracker.dto.note.ApplicationNoteResponse;
import com.jobtracker.entity.User;
import com.jobtracker.service.ApplicationNoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications/{applicationId}/notes")
@RequiredArgsConstructor
public class ApplicationNoteController {

    private final ApplicationNoteService applicationNoteService;

    @GetMapping
    public ResponseEntity<Page<ApplicationNoteResponse>> list(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long applicationId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(applicationNoteService.list(currentUser.getId(), applicationId, pageable));
    }

    @PostMapping
    public ResponseEntity<ApplicationNoteResponse> create(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationNoteRequest request
    ) {
        ApplicationNoteResponse created = applicationNoteService.create(
                currentUser.getId(), currentUser, applicationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long applicationId,
            @PathVariable Long noteId
    ) {
        applicationNoteService.delete(currentUser.getId(), noteId);
        return ResponseEntity.noContent().build();
    }
}
