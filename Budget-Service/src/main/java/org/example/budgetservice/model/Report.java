package org.example.budgetservice.model;

import java.time.LocalDate;
import java.util.Map;

public interface Report {
    Long getReportId();
    void setReportId(Long reportId);

    Long getUserId();
    void setUserId(Long userId);

    Map<String, Double> getInfo();
    void setInfo(Map<String, Double> info);

    LocalDate getReportDate();
    void setReportDate(LocalDate reportDate);


}
