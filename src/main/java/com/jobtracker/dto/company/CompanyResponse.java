package com.jobtracker.dto.company;

import com.jobtracker.entity.Company;

public record CompanyResponse(
        Long id,
        String name,
        String websiteUrl,
        String careerPageUrl,
        String notes
) {
    public static CompanyResponse from(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getWebsiteUrl(),
                company.getCareerPageUrl(),
                company.getNotes()
        );
    }
}
