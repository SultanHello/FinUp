package org.example.budgetservice.service;


import lombok.AllArgsConstructor;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.notification.NotificationSender;
import org.example.budgetservice.repository.ReportDailyRepository;
import org.example.budgetservice.repository.ReportWeeklyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BudgetService {
    private final ReportService<ReportWeekly> reportWeeklyReportService;
    private final ReportService<ReportDaily> reportDailyReportService;



    public ReportWeekly getReportWeeklyByReportId(Long id) {
        return reportWeeklyReportService.getReportByReportId(id);
    }

    public ReportWeekly getLastReportWeekly(Long id) {
        return reportWeeklyReportService.getLastReport(id);
    }

    public void saveReportWeekly(Map<String,Double> reportInfo,Long id){
        reportWeeklyReportService.saveReport(reportInfo,id);
    }


    public List<ReportWeekly> getReportsWeekly() {
        return reportWeeklyReportService.getAllReports();
    }

    public List<ReportWeekly> getReportWeeklyByUserId(Long userId) {
        return reportWeeklyReportService.getReportByUserId(userId);
    }

    public void saveReportDaily(Map<String,Double> reportInfo, Long id) {
        reportDailyReportService.saveReport(reportInfo,id);
    }



    public List<ReportDaily> getReportDailyByUserId(Long userId) {
        return reportDailyReportService.getReportByUserId(userId);
    }

    public ReportDaily getReportDailyByReportId(Long reportId) {
        return reportDailyReportService.getReportByReportId(reportId);
    }

    public List<ReportDaily> getReportsDaily() {
        return reportDailyReportService.getAllReports();
    }

    public ReportDaily getLastReportDaily(Long userId) {
        return reportDailyReportService.getLastReport(userId);
    }
}
