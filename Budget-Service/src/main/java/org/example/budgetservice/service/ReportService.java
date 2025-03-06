package org.example.budgetservice.service;

import java.util.List;
import java.util.Map;

public interface ReportService<T> {
    T getReportByReportId(Long id);
    T getLastReport(Long userId);
    List<T> getReportByUserId(Long userId);
    void saveReport(Map<String, Double> reportInfo, Long userId);
    List<T> getAllReports();
}
