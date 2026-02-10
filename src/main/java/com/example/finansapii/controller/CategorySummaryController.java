package com.example.finansapii.controller;

import com.example.finansapii.dto.CategorySummaryView;
import com.example.finansapii.service.CategorySummaryService;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/categorysummary")
public class CategorySummaryController {

    private final CategorySummaryService service;

    public CategorySummaryController(CategorySummaryService service) {
        this.service = service;
    }

    // GET /api/categorysummary/monthly?yilAy=2026-02
    // Header: X-USER-ID: 7
    // (Tarayıcı testi için opsiyonel) ?kullaniciId=7
    @GetMapping("/monthly")
    public List<CategorySummaryView> monthly(
            @RequestHeader(value = "X-USER-ID", required = false) Long headerUserId,
            @RequestParam(value = "kullaniciId", required = false) Long queryUserId,
            @RequestParam String yilAy
    ) {
        Long kullaniciId = (headerUserId != null) ? headerUserId : queryUserId;
        if (kullaniciId == null) {
            throw new IllegalArgumentException("X-USER-ID header veya kullaniciId param zorunlu");
        }

        return service.getMonthlySummary(kullaniciId, YearMonth.parse(yilAy));
    }
}
