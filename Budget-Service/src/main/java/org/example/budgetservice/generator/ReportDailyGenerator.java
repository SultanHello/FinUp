
package org.example.budgetservice.generator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.budgetservice.service.ReportDailyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportDailyGenerator implements ReportGenerator {

    private final ReportDailyService reportDailyService;

    /**
     * Генерация ежедневного отчета каждый день в 00:05
     */
    @Scheduled(cron = "0 5 0 * * *")
    @Override
    public void generateReport() {
        log.info("Starting daily report generation...");

        try {
            addReport();
            log.info("Daily report generation completed successfully");
        } catch (Exception e) {
            log.error("Daily report generation failed", e);
        }
    }

    public void addReport() {
        List<Long> userIds = reportDailyService.getAllUserIds();

        if (userIds.isEmpty()) {
            log.warn("No users found for daily report generation");
            return;
        }

        log.info("Generating daily reports for {} users", userIds.size());

        int successCount = 0;
        int failCount = 0;

        for (Long userId : userIds) {
            try {
                reportDailyService.saveReportForUser(userId);
                successCount++;
            } catch (Exception e) {
                log.error("Failed to generate daily report for user: {}", userId, e);
                failCount++;
            }
        }

        log.info("Daily report generation finished. Success: {}, Failed: {}", successCount, failCount);
    }
}
