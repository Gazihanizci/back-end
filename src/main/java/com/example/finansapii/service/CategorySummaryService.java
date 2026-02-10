package com.example.finansapii.service;

import com.example.finansapii.dto.CategorySummaryView;
import com.example.finansapii.repository.IslemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class CategorySummaryService {

    private final IslemRepository islemRepository;

    public CategorySummaryService(IslemRepository islemRepository) {
        this.islemRepository = islemRepository;
    }

    public List<CategorySummaryView> getMonthlySummary(Long kullaniciId, YearMonth yearMonth) {
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return islemRepository.sumByCategoryUserMonthly(kullaniciId, start, end);
    }
}
