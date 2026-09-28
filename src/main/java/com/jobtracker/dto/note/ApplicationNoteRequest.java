package com.jobtracker.dto.note;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record ApplicationNoteRequest(

        @NotBlank
        String noteType,

        @NotBlank
        String content,

        String contactPerson,

        LocalDateTime scheduledDate
) {
}
