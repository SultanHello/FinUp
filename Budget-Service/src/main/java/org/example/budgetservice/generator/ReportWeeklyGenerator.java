package org.example.budgetservice.generator;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;

import org.example.budgetservice.service.BudgetService;
import org.example.budgetservice.service.ReportWeeklyService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportWeeklyGenerator implements ReportGenerator{

    private final BudgetService budgetService;
    private final TransactionClient transactionClient;
    private final ReportWeeklyService reportWeeklyService;

    // Запуск каждое воскресенье в полночь
    @Scheduled(cron = "0 0 1 * * SUN")
    public void generateReport() {
        addReport();
        // Логика генерации отчета
        System.out.println("Генерация еженедельного отчета...");
        // Здесь можешь собирать данные и отправлять их на email или сохранять в базу
    }
    public Map<String,Double> weeklyReport(Long id){
        return transactionClient.getReportWeekly(id);
    }
    public void addReport() {
        List<Long> ids = reportWeeklyService.getAllUserIds();
        for (Long id : ids) {
            reportWeeklyService.saveReportForUser(id);
            budgetService.saveReportWeekly(weeklyReport(id),id);
        }
    }
}
