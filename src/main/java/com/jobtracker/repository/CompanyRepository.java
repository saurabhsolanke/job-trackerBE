package com.jobtracker.repository;

import com.jobtracker.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Companies are shared reference data, not tenant-scoped, so no user_id
 * filtering applies here (see Company entity javadoc).
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
