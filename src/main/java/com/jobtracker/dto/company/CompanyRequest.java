package com.jobtracker.dto.company;

import jakarta.validation.constraints.NotBlank;

public record CompanyRequest(

        @NotBlank
        String name,

        String websiteUrl,

        String careerPageUrl,

        String notes
) {
}
