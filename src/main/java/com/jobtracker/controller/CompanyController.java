package com.jobtracker.controller;

import com.jobtracker.dto.company.CompanyRequest;
import com.jobtracker.dto.company.CompanyResponse;
import com.jobtracker.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> search(
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        return ResponseEntity.ok(companyService.search(q, pageable));
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> create(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(companyService.create(request));
    }
}
