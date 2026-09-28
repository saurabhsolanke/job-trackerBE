package com.jobtracker.repository;

import com.jobtracker.entity.ApplicationNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DATA ISOLATION: same pattern as JobApplicationRepository - all lookups are
 * scoped by userId at the query level so a note belonging to another tenant
 * is simply invisible, never returned then filtered out in Java.
 */
public interface ApplicationNoteRepository extends JpaRepository<ApplicationNote, Long> {

    Page<ApplicationNote> findByUserIdAndApplicationId(Long userId, Long applicationId, Pageable pageable);

    Optional<ApplicationNote> findByUserIdAndId(Long userId, Long id);
}
