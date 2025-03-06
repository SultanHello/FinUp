package org.example.userservice.service;

import org.example.userservice.dto.ReportDTO;
import org.example.userservice.model.User;

import java.util.List;

public interface ReportServiceInterface {
    String getAdviceWeekly(String token);
    List<ReportDTO> getReportWeekly(String token);
    ReportDTO getReportWeeklyLast(String token);

    String getAdviceDaily(String token);
    ReportDTO getReportDailyLast(String token);
    List<ReportDTO> getReportDaily(String token);

}