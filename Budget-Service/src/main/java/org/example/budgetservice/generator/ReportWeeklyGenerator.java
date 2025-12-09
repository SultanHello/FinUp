package org.example.budgetservice.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.budgetservice.service.ReportWeeklyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportWeeklyGenerator implements ReportGenerator {

    private final ReportWeeklyService reportWeeklyService;

    /**
     * Генерация еженедельного отчета каждое воскресенье в 01:00
     */
    @Scheduled(cron = "0 0 1 * * SUN")
    @Override
    public void generateReport() {
        log.info("Starting weekly report generation...");

        try {
            addReport();
            log.info("Weekly report generation completed successfully");
        } catch (Exception e) {
            log.error("Weekly report generation failed", e);
        }
    }

    public void addReport() {
        List<Long> userIds = reportWeeklyService.getAllUserIds();

        if (userIds.isEmpty()) {
            log.warn("No users found for weekly report generation");
            return;
        }

        log.info("Generating weekly reports for {} users", userIds.size());

        int successCount = 0;
        int failCount = 0;

        for (Long userId : userIds) {
            try {
                reportWeeklyService.saveReportForUser(userId);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to generate weekly report for user: {}", userId, e);
                failCount++;
            }
        }

        log.info("Weekly report generation finished. Success: {}, Failed: {}", successCount, failCount);
    }
}