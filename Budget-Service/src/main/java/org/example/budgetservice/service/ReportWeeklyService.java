package org.example.budgetservice.service;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;
import org.example.budgetservice.factory.ReportFactory;
import org.example.budgetservice.generator.ReportWeeklyGenerator;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.notification.NotificationSender;

import org.example.budgetservice.observer.ReportObserver;
import org.example.budgetservice.repository.ReportWeeklyRepository;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ReportWeeklyService implements ReportService<ReportWeekly> {
    private final ReportWeeklyRepository reportWeeklyRepository;
    private final ReportObserver reportObserver;
    private final ReportFactory reportFactory;
    private final TransactionClient transactionClient;
    private final UserClient userClient;
    @Override
    public List<ReportWeekly> getAllReports() {
        return reportWeeklyRepository.findAll();
    }

    @Override
    public ReportWeekly getReportByReportId(Long id) {
        return reportWeeklyRepository.findByReportId(id);
    }

    @Override
    public ReportWeekly getLastReport(Long userId) {
        List<ReportWeekly> reportWeeklies = reportWeeklyRepository.findByUserId(userId);
        if(reportWeeklies.isEmpty()){
            return null;
        }
        ReportWeekly lastReportWeekly = reportWeeklies.get(reportWeeklies.size()-1);
        return lastReportWeekly;
    }

    @Override
    public void saveReport(Map<String, Double> reportInfo, Long userId) {
        ReportWeekly reportWeekly = reportFactory.createWeeklyReport(reportInfo,userId);
        reportObserver.onReportCreated(reportWeekly);
        reportWeeklyRepository.save(reportWeekly);

    }
    @Override
    public List<ReportWeekly> getReportByUserId(Long userId) {
        return reportWeeklyRepository.findByUserId(userId);
    }


    public List<Long> getAllUserIds() {
        return userClient.userIds();
    }

    public void saveReportForUser(Long userId) {
        Map<String,Double> reportData=transactionClient.getReportWeekly(userId);

        saveReport(reportData, userId);
    }
}
