package com.example.finansapii.controller;

import com.example.finansapii.dto.CategorySummaryView;
import com.example.finansapii.security.CurrentUser;
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
    @GetMapping("/monthly")
    public List<CategorySummaryView> monthly(@RequestParam String yilAy) {
        Long kullaniciId = CurrentUser.id();
        return service.getMonthlySummary(kullaniciId, YearMonth.parse(yilAy));
    }
}
