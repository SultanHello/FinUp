package org.example.budgetservice.factory;

import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ReportFactory {
    public ReportWeekly createWeeklyReport(Map<String, Double> info, Long userId) {
        return ReportWeekly.builder()
                .userId(userId)
                .info(info)
                .reportDate(LocalDate.now())
                .build();
    }
    public ReportDaily createDailyReport(Map<String, Double> info, Long userId) {
        return ReportDaily.builder()
                .userId(userId)
                .info(info)
                .reportDate(LocalDate.now())
                .build();
    }
}