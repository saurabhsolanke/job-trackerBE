package com.jobtracker.repository;

import com.jobtracker.entity.ApplicationStatus;
import com.jobtracker.entity.JobApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DATA ISOLATION: every finder here takes userId as an explicit parameter and
 * filters on it directly, rather than trusting a userId that could otherwise
 * be forged/mismatched at the service layer. A caller can never fetch, page
 * through, or mutate a row belonging to another user through this repository -
 * findByUserIdAndId() simply returns empty for a foreign id, which the
 * service layer turns into a 404 (see JobApplicationService).
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    Page<JobApplication> findByUserId(Long userId, Pageable pageable);

    Page<JobApplication> findByUserIdAndStatus(Long userId, ApplicationStatus status, Pageable pageable);

    Optional<JobApplication> findByUserIdAndId(Long userId, Long id);

    boolean existsByUserIdAndId(Long userId, Long id);
}
