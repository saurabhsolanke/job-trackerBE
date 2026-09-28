package com.jobtracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Companies are shared reference data (no user_id) - multiple users can log
 * applications against the same company row, e.g. "Google".
 */
@Entity
@Table(name = "companies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "website_url")
    private String websiteUrl;

    @Column(name = "career_page_url")
    private String careerPageUrl;

    @Column(columnDefinition = "text")
    private String notes;
}
