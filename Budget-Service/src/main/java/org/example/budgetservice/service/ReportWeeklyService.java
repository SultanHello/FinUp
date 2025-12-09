package org.example.budgetservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;
import org.example.budgetservice.factory.ReportFactory;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.observer.ReportObserver;
import org.example.budgetservice.repository.ReportWeeklyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportWeeklyService implements ReportService<ReportWeekly> {

    private final ReportWeeklyRepository reportWeeklyRepository;
    private final ReportObserver reportObserver;
    private final ReportFactory reportFactory;
    private final TransactionClient transactionClient;
    private final UserClient userClient;

    @Override
    @Transactional(readOnly = true)
    public List<ReportWeekly> getAllReports() {
        return reportWeeklyRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportWeekly getReportByReportId(Long id) {
        return reportWeeklyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Weekly report not found with id: " + id));
    }

    /**
     * ✅ ИСПРАВЛЕНО: Использует оптимизированный запрос
     */
    @Override
    @Transactional(readOnly = true)
    public ReportWeekly getLastReport(Long userId) {
        return reportWeeklyRepository
                .findFirstByUserIdOrderByReportDateDescReportIdDesc(userId)
                .orElse(null);
    }

    @Override
    @Transactional
    public void saveReport(Map<String, Double> reportInfo, Long userId) {
        if (reportInfo == null || reportInfo.isEmpty()) {
            log.warn("Skipping empty weekly report for user: {}", userId);
            return;
        }

        // ✅ НОВАЯ ПРОВЕРКА: Не создаём дубликаты за одну неделю
        LocalDate today = LocalDate.now();
        if (reportWeeklyRepository.existsByUserIdAndReportDate(userId, today)) {
            log.warn("Weekly report already exists for user {} for date {}", userId, today);
            return;
        }

        try {
            ReportWeekly report = reportFactory.createWeeklyReport(reportInfo, userId);
            reportWeeklyRepository.save(report);

            // Уведомляем наблюдателей после успешного сохранения
            reportObserver.onReportCreated(report);

            log.info("Weekly report saved for user: {}, report id: {}", userId, report.getReportId());
        } catch (Exception e) {
            log.error("Failed to save weekly report for user: {}", userId, e);
            throw new RuntimeException("Failed to save weekly report", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReportWeekly> getReportByUserId(Long userId) {
        return reportWeeklyRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Long> getAllUserIds() {
        return userClient.userIds();
    }

    @Transactional
    public void saveReportForUser(Long userId) {
        try {
            Map<String, Double> reportData = transactionClient.getReportWeekly(userId);

            if (reportData == null || reportData.isEmpty()) {
                log.info("No transactions found for weekly report, user: {}", userId);
                return;
            }

            saveReport(reportData, userId);
        } catch (Exception e) {
            log.error("Failed to generate weekly report for user: {}", userId, e);
            // Не прерываем выполнение для других пользователей
        }
    }
}