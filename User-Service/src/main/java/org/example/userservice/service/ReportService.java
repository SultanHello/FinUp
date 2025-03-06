package org.example.userservice.service;

import lombok.AllArgsConstructor;
import org.example.userservice.client.BudgetClient;
import org.example.userservice.dto.ReportDTO;
import org.example.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ReportService implements ReportServiceInterface {
    private final UserService userService;
    private final BudgetClient budgetClient;
    @Override
    public String getAdviceWeekly(String token) {
        return null;
    }
    @Override
    public List<ReportDTO> getReportWeekly(String token) {
        Long userId = userService.getUserId(token);
        return budgetClient.getReportsWeekly(userId);
    }

    @Override
    public ReportDTO getReportWeeklyLast(String token) {
        Long userId = userService.getUserId(token);
        return budgetClient.getReportWeeklyLast(userId);
    }

    @Override
    public ReportDTO getReportDailyLast(String token) {
        Long userId = userService.getUserId(token);
        return budgetClient.getReportDailyLast(userId);
    }

    @Override
    public List<ReportDTO> getReportDaily(String token) {
        Long userId = userService.getUserId(token);
        return budgetClient.getReportsDaily(userId);
    }


    @Override
    public String getAdviceDaily(String token) {
        Long userId = userService.getUserId(token);
        return null;
    }


}