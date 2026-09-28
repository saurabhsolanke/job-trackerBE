package com.jobtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Core tenant-scoped entity. Every row carries a hard FK to the owning user
 * (user_id) so repository queries can filter by owner instead of relying on
 * service-layer checks alone - see JobApplicationRepository.
 */
@Entity
@Table(name = "job_applications")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "job_title", nullable = false)
    private String jobTitle;

    @Column(name = "job_location")
    private String jobLocation;

    @Column(name = "job_url")
    private String jobUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status;

    @Column(name = "date_applied")
    private LocalDate dateApplied;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @Column(name = "cover_letter_path")
    private String coverLetterPath;

    @Column(name = "resume_snapshot_path")
    private String resumeSnapshotPath;

    // ---- Referral tracking ----

    @Column(name = "referral_name")
    private String referralName;

    @Column(name = "referral_contact")
    private String referralContact;

    @Column(name = "source_channel")
    private String sourceChannel;

    @Column(name = "source_url")
    private String sourceUrl;

    // Explicit TEXT column (not @Lob) so PostgreSQL stores this inline as
    // text rather than as a large object (OID), which is awkward to query/index.
    @Column(name = "job_description", columnDefinition = "text")
    private String jobDescription;

    @PrePersist
    protected void onCreate() {
        this.lastUpdated = LocalDateTime.now();
        if (this.status == null) {
            this.status = ApplicationStatus.WISHLIST;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}
