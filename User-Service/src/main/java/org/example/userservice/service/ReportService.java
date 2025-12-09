package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.userservice.client.BudgetClient;
import org.example.userservice.dto.ReportDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService implements ReportServiceInterface {

    private final UserService userService;
    private final BudgetClient budgetClient;

    @Override
    public List<ReportDTO> getReportWeekly(String token) {
        Long userId = userService.getUserId(token);
        log.info("Fetching weekly reports for user: {}", userId);

        try {
            return budgetClient.getReportsWeekly(userId);
        } catch (Exception e) {
            log.error("Failed to fetch weekly reports for user: {}", userId, e);
            throw new RuntimeException("Failed to fetch weekly reports", e);
        }
    }

    @Override
    public ReportDTO getReportWeeklyLast(String token) {
        Long userId = userService.getUserId(token);
        log.info("Fetching last weekly report for user: {}", userId);

        try {
            ReportDTO report = budgetClient.getReportWeeklyLast(userId);
            if (report == null) {
                log.warn("No weekly reports found for user: {}", userId);
            }
            return report;
        } catch (Exception e) {
            log.error("Failed to fetch last weekly report for user: {}", userId, e);
            throw new RuntimeException("Failed to fetch last weekly report", e);
        }
    }

    @Override
    public String getAdviceWeekly(String token) {
        ReportDTO lastReport = getReportWeeklyLast(token);

        if (lastReport == null || lastReport.getAdvice() == null) {
            log.warn("No advice available in weekly report");
            return "No advice available. Please generate a weekly report first.";
        }

        return lastReport.getAdvice();
    }

    @Override
    public List<ReportDTO> getReportDaily(String token) {
        Long userId = userService.getUserId(token);
        log.info("Fetching daily reports for user: {}", userId);

        try {
            return budgetClient.getReportsDaily(userId);
        } catch (Exception e) {
            log.error("Failed to fetch daily reports for user: {}", userId, e);
            throw new RuntimeException("Failed to fetch daily reports", e);
        }
    }

    @Override
    public ReportDTO getReportDailyLast(String token) {
        Long userId = userService.getUserId(token);
        log.info("Fetching last daily report for user: {}", userId);

        try {
            ReportDTO report = budgetClient.getReportDailyLast(userId);
            if (report == null) {
                log.warn("No daily reports found for user: {}", userId);
            }
            return report;
        } catch (Exception e) {
            log.error("Failed to fetch last daily report for user: {}", userId, e);
            throw new RuntimeException("Failed to fetch last daily report", e);
        }
    }

    @Override
    public String getAdviceDaily(String token) {
        ReportDTO lastReport = getReportDailyLast(token);

        if (lastReport == null || lastReport.getAdvice() == null) {
            log.warn("No advice available in daily report");
            return "No advice available. Please generate a daily report first.";
        }

        return lastReport.getAdvice();
    }
}