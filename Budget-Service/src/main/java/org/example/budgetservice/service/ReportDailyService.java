package org.example.budgetservice.service;

import lombok.AllArgsConstructor;
import org.example.budgetservice.factory.ReportFactory;
import org.example.budgetservice.model.Report;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.repository.ReportDailyRepository;
import org.example.budgetservice.repository.ReportWeeklyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@AllArgsConstructor

@Service
public class ReportDailyService implements ReportService<ReportDaily>{
    private final ReportDailyRepository reportDailyRepository;
    private final ReportFactory reportFactory;
    @Override
    public ReportDaily getReportByReportId(Long id) {
        return reportDailyRepository.findByReportId(id);
    }

    @Override
    public ReportDaily getLastReport(Long userId) {
        List<ReportDaily> reportDailies = reportDailyRepository.findByUserId(userId);
        if(reportDailies.isEmpty()){
            return null;
        }
        ReportDaily lastReportDaily = reportDailies.get(reportDailies.size()-1);
        return lastReportDaily;
    }

    @Override
    public List<ReportDaily> getReportByUserId(Long userId) {
        return reportDailyRepository.findByUserId(userId);
    }

    @Override
    public void saveReport(Map<String, Double> reportInfo, Long userId) {
        ReportDaily reportDaily = reportFactory.createDailyReport(reportInfo,userId);
        reportDailyRepository.save(reportDaily);

    }

    @Override
    public List<ReportDaily> getAllReports() {
        return reportDailyRepository.findAll();
    }
}
