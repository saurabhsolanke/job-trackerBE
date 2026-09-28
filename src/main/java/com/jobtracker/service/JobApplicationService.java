package com.jobtracker.service;

import com.jobtracker.dto.application.JobApplicationRequest;
import com.jobtracker.dto.application.JobApplicationResponse;
import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.Company;
import com.jobtracker.entity.JobApplication;
import com.jobtracker.entity.User;
import com.jobtracker.exception.ResourceNotFoundException;
import com.jobtracker.repository.CompanyRepository;
import com.jobtracker.repository.JobApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CompanyRepository companyRepository;

    /**
     * DATA ISOLATION: paging is done through findByUserId / findByUserIdAndStatus,
     * so the SQL WHERE clause itself excludes every other tenant's rows - there is
     * no in-memory filtering step that could be forgotten.
     */
    @Transactional(readOnly = true)
    public Page<JobApplicationResponse> list(Long userId, ApplicationStatus status, Pageable pageable) {
        Page<JobApplication> page = (status != null)
                ? jobApplicationRepository.findByUserIdAndStatus(userId, status, pageable)
                : jobApplicationRepository.findByUserId(userId, pageable);

        return page.map(JobApplicationResponse::from);
    }

    @Transactional(readOnly = true)
    public JobApplicationResponse getById(Long userId, Long id) {
        JobApplication application = findOwnedOrThrow(userId, id);
        return JobApplicationResponse.from(application);
    }

    @Transactional
    public JobApplicationResponse create(Long userId, User currentUser, JobApplicationRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + request.companyId()));

        JobApplication application = JobApplication.builder()
                .user(currentUser) // owner is always taken from the authenticated principal, never from the request body
                .company(company)
                .jobTitle(request.jobTitle())
                .jobLocation(request.jobLocation())
                .jobUrl(request.jobUrl())
                .status(request.status())
                .dateApplied(request.dateApplied())
                .coverLetterPath(request.coverLetterPath())
                .resumeSnapshotPath(request.resumeSnapshotPath())
                .referralName(request.referralName())
                .referralContact(request.referralContact())
                .sourceChannel(request.sourceChannel())
                .sourceUrl(request.sourceUrl())
                .jobDescription(request.jobDescription())
                .build();

        JobApplication saved = jobApplicationRepository.save(application);
        return JobApplicationResponse.from(saved);
    }

    @Transactional
    public JobApplicationResponse update(Long userId, Long id, JobApplicationRequest request) {
        JobApplication application = findOwnedOrThrow(userId, id);

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new ResourceNotFoundException("Company not found: " + request.companyId()));

        application.setCompany(company);
        application.setJobTitle(request.jobTitle());
        application.setJobLocation(request.jobLocation());
        application.setJobUrl(request.jobUrl());
        application.setStatus(request.status());
        application.setDateApplied(request.dateApplied());
        application.setCoverLetterPath(request.coverLetterPath());
        application.setResumeSnapshotPath(request.resumeSnapshotPath());
        application.setReferralName(request.referralName());
        application.setReferralContact(request.referralContact());
        application.setSourceChannel(request.sourceChannel());
        application.setSourceUrl(request.sourceUrl());
        application.setJobDescription(request.jobDescription());

        // No explicit save() needed - `application` is a managed entity inside
        // this @Transactional method, so Hibernate flushes changes on commit.
        return JobApplicationResponse.from(application);
    }

    @Transactional
    public void delete(Long userId, Long id) {
        JobApplication application = findOwnedOrThrow(userId, id);
        jobApplicationRepository.delete(application);
    }

    /**
     * Central ownership check: a single userId-scoped query decides both
     * "does this id exist" and "does it belong to the caller" at once. A
     * mismatch (foreign id, or no such id) is intentionally indistinguishable
     * and always surfaces as 404 - never as 403 - so we don't leak the
     * existence of other users' records.
     */
    private JobApplication findOwnedOrThrow(Long userId, Long id) {
        return jobApplicationRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found: " + id));
    }
}
