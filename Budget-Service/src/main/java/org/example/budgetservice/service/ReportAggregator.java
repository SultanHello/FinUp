package org.example.budgetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.repository.ReportDailyRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReportAggregator {

    private final ReportDailyRepository reportDailyRepository;


    public List<ReportDaily> reportsDailyToday() {
        LocalDate today = LocalDate.now();
        log.debug("Fetching reports for today: {}", today);
        return reportDailyRepository.findAllByReportDate(today);
    }

    public List<ReportDaily> reportsDailyWeek() {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today; // ✅ До сегодня, не до воскресенья!

        log.debug("Fetching reports for current week: {} to {}", startOfWeek, endOfWeek);
        return reportDailyRepository.findAllByReportDateBetween(startOfWeek, endOfWeek);
    }

    public List<ReportDaily> reportsDailyLastCompleteWeek() {
        LocalDate today = LocalDate.now();
        LocalDate lastSunday = today.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        LocalDate lastMonday = lastSunday.minusDays(6); // 7 дней назад от воскресенья

        log.debug("Fetching reports for last complete week: {} to {}", lastMonday, lastSunday);
        return reportDailyRepository.findAllByReportDateBetween(lastMonday, lastSunday);
    }


    public List<ReportDaily> reportsDailyWeekByUser(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today;

        log.debug("Fetching reports for user {} for current week: {} to {}",
                userId, startOfWeek, endOfWeek);

        return reportDailyRepository.findByUserIdAndReportDateBetween(userId, startOfWeek, endOfWeek);
    }


    public List<ReportDaily> reportsDailyLastNDays(int days) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        log.debug("Fetching reports for last {} days: {} to {}", days, startDate, today);
        return reportDailyRepository.findAllByReportDateBetween(startDate, today);
    }


    public List<ReportDaily> reportsDailyLastNDaysByUser(Long userId, int days) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(days - 1);

        log.debug("Fetching reports for user {} for last {} days: {} to {}",
                userId, days, startDate, today);

        return reportDailyRepository.findByUserIdAndReportDateBetween(userId, startDate, today);
    }
}