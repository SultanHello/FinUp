package org.example.budgetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;
import org.example.budgetservice.factory.ReportFactory;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.repository.ReportDailyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportDailyService implements ReportService<ReportDaily> {

    private final ReportDailyRepository reportDailyRepository;
    private final ReportFactory reportFactory;
    private final UserClient userClient;
    private final TransactionClient transactionClient;

    @Override
    @Transactional(readOnly = true)
    public ReportDaily getReportByReportId(Long id) {
        return reportDailyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Daily report not found with id: " + id));
    }

    /**
     * ✅ ИСПРАВЛЕНО: Использует оптимизированный запрос из репозитория
     */
    @Override
    @Transactional(readOnly = true)
    public ReportDaily getLastReport(Long userId) {
        return reportDailyRepository
                .findFirstByUserIdOrderByReportDateDescReportIdDesc(userId)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDaily> getReportByUserId(Long userId) {
        return reportDailyRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public void saveReport(Map<String, Double> reportInfo, Long userId) {
        if (reportInfo == null || reportInfo.isEmpty()) {
            log.warn("Skipping empty daily report for user: {}", userId);
            return;
        }

        // ✅ НОВАЯ ПРОВЕРКА: Не создаём дубликаты за один день
        LocalDate today = LocalDate.now();
        if (reportDailyRepository.existsByUserIdAndReportDate(userId, today)) {
            log.warn("Daily report already exists for user {} for date {}", userId, today);
            return;
        }

        try {
            ReportDaily report = reportFactory.createDailyReport(reportInfo, userId);
            reportDailyRepository.save(report);
            log.info("Daily report saved for user: {}, report id: {}", userId, report.getReportId());
        } catch (Exception e) {
            log.error("Failed to save daily report for user: {}", userId, e);
            throw new RuntimeException("Failed to save daily report", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportDaily> getAllReports() {
        return reportDailyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Long> getAllUserIds() {
        return userClient.userIds();
    }

    @Transactional
    public void saveReportForUser(Long userId) {
        try {
            Map<String, Double> reportData = transactionClient.getReportDaily(userId);

            if (reportData == null || reportData.isEmpty()) {
                log.info("No transactions found for daily report, user: {}", userId);
                return;
            }

            saveReport(reportData, userId);
        } catch (Exception e) {
            log.error("Failed to generate daily report for user: {}", userId, e);
            // Не прерываем выполнение для других пользователей
        }
    }
}