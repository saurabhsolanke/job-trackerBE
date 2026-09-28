package com.jobtracker.dto.application;

import com.jobtracker.dto.company.CompanyResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO deliberately omits the raw user_id / company_id FKs and the
 * User entity (which carries passwordHash) - only the fields the client
 * should ever see are exposed here.
 */
public record JobApplicationResponse(
        Long id,
        CompanyResponse company,
        String jobTitle,
        String jobLocation,
        String jobUrl,
        ApplicationStatus status,
        LocalDate dateApplied,
        LocalDateTime lastUpdated,
        String coverLetterPath,
        String resumeSnapshotPath,
        String referralName,
        String referralContact,
        String sourceChannel,
        String sourceUrl,
        String jobDescription
) {
    public static JobApplicationResponse from(JobApplication app) {
        return new JobApplicationResponse(
                app.getId(),
                CompanyResponse.from(app.getCompany()),
                app.getJobTitle(),
                app.getJobLocation(),
                app.getJobUrl(),
                app.getStatus(),
                app.getDateApplied(),
                app.getLastUpdated(),
                app.getCoverLetterPath(),
                app.getResumeSnapshotPath(),
                app.getReferralName(),
                app.getReferralContact(),
                app.getSourceChannel(),
                app.getSourceUrl(),
                app.getJobDescription()
        );
    }
}
