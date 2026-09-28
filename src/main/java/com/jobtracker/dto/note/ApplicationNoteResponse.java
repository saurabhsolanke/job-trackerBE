package com.jobtracker.dto.note;

import com.jobtracker.entity.ApplicationNote;

import java.time.LocalDateTime;

public record ApplicationNoteResponse(
        Long id,
        Long applicationId,
        String noteType,
        String content,
        String contactPerson,
        LocalDateTime scheduledDate,
        LocalDateTime createdAt
) {
    public static ApplicationNoteResponse from(ApplicationNote note) {
        return new ApplicationNoteResponse(
                note.getId(),
                note.getApplication().getId(),
                note.getNoteType(),
                note.getContent(),
                note.getContactPerson(),
                note.getScheduledDate(),
                note.getCreatedAt()
        );
    }
}
