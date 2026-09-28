package com.jobtracker.service;

import com.jobtracker.dto.note.ApplicationNoteRequest;
import com.jobtracker.dto.note.ApplicationNoteResponse;
import com.jobtracker.entity.ApplicationNote;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.ApplicationNoteRepository;
import com.jobtracker.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationNoteService {

    private final ApplicationNoteRepository noteRepository;
    private final JobApplicationRepository jobApplicationRepository;

    @Transactional(readOnly = true)
    public Page<ApplicationNoteResponse> list(Long userId, Long applicationId, Pageable pageable) {
        // Confirms the parent application belongs to the caller before returning
        // any notes, so a foreign applicationId can't be used to enumerate notes.
        assertApplicationOwnedByUser(userId, applicationId);

        return noteRepository.findByUserIdAndApplicationId(userId, applicationId, pageable)
                .map(ApplicationNoteResponse::from);
    }

    @Transactional
    public ApplicationNoteResponse create(Long userId, User currentUser, Long applicationId, ApplicationNoteRequest request) {
        JobApplication application = jobApplicationRepository.findByUserIdAndId(userId, applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + applicationId));

        ApplicationNote note = ApplicationNote.builder()
                .application(application)
                .user(currentUser)
                .noteType(request.noteType())
                .content(request.content())
                .contactPerson(request.contactPerson())
                .scheduledDate(request.scheduledDate())
                .build();

        return ApplicationNoteResponse.from(noteRepository.save(note));
    }

    @Transactional
    public void delete(Long userId, Long noteId) {
        ApplicationNote note = noteRepository.findByUserIdAndId(userId, noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found: " + noteId));
        noteRepository.delete(note);
    }

    private void assertApplicationOwnedByUser(Long userId, Long applicationId) {
        if (!jobApplicationRepository.existsByUserIdAndId(userId, applicationId)) {
            throw new ResourceNotFoundException("Job application not found: " + applicationId);
        }
    }
}
