package com.jobtracker.dto.application;

import com.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Note there is no userId field here on purpose - the owning user is always
 * derived from the authenticated JWT principal in the service layer, never
 * taken from client input (see JobApplicationService.create/update).
 */
public record JobApplicationRequest(

        @NotNull
        Long companyId,

        @NotBlank
        String jobTitle,

        String jobLocation,

        String jobUrl,

        @NotNull
        ApplicationStatus status,

        LocalDate dateApplied,

        String coverLetterPath,

        String resumeSnapshotPath,

        String referralName,

        String referralContact,

        String sourceChannel,

        String sourceUrl,

        String jobDescription
) {
}
