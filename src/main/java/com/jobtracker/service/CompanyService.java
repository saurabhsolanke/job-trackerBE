package com.jobtracker.service;

import com.jobtracker.dto.company.CompanyRequest;
import com.jobtracker.dto.company.CompanyResponse;
import com.jobtracker.entity.Company;
import com.jobtracker.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Companies are shared reference data (see Company entity), so unlike the
 * other services here, no userId scoping applies - any authenticated user
 * can search/create companies to attach their own applications to.
 */
@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Page<CompanyResponse> search(String query, Pageable pageable) {
        Page<Company> page = (query != null && !query.isBlank())
                ? companyRepository.findByNameContainingIgnoreCase(query, pageable)
                : companyRepository.findAll(pageable);

        return page.map(CompanyResponse::from);
    }

    @Transactional
    public CompanyResponse create(CompanyRequest request) {
        Company company = Company.builder()
                .name(request.name())
                .websiteUrl(request.websiteUrl())
                .careerPageUrl(request.careerPageUrl())
                .notes(request.notes())
                .build();

        return CompanyResponse.from(companyRepository.save(company));
    }
}
