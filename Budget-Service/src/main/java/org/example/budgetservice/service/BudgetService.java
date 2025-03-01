package org.example.budgetservice.service;


import lombok.AllArgsConstructor;
import org.example.budgetservice.model.Report;
import org.example.budgetservice.notification.NotificationSender;
import org.example.budgetservice.repository.ReportRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BudgetService {

    private final ReportRepository reportRepository;
    private final NotificationSender notificationSender;


    public Report getReportByReportId(Long id) {
        return reportRepository.findByReportId(id);
    }

    public Report getLastReport(Long id) {
        List<Report> reports = reportRepository.findByUserId(id);
        Report lastReport = reports.get(reports.size()-1);
        return lastReport;
    }

    public void saveReport(Map<String,Double> reportInfo,Long id){
        Report report = Report.builder()
                .userId(id)
                .info(reportInfo)
                .reportDate(LocalDate.now())
                .build();
        sendNotification(report);
        reportRepository.save(report);
    }
    private void sendNotification(Report report){
        notificationSender.send(report);
    }

    public List<Report> getReports() {
        return reportRepository.findAll();
    }

    public List<Report> getReportByUserId(Long userId) {
        return reportRepository.findByUserId(userId);
    }
}
