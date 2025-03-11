package org.example.budgetservice.service;

import lombok.AllArgsConstructor;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.repository.ReportDailyRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class ReportAggregator {
    private final ReportDailyRepository reportDailyRepository;
    public List<ReportDaily> reportsDailyToday(){
        LocalDate localDate = LocalDate.now();
        return reportDailyRepository.findAllByReportDate(localDate);
    }
    public List<ReportDaily> reportsDailyWeek(){
        LocalDate start = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate end = LocalDate.now().with(DayOfWeek.SUNDAY);
        return reportDailyRepository.findAllByReportDateBetween(start, end);
    }
}
