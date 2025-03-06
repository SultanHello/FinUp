package org.example.budgetservice.generator;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;

import org.example.budgetservice.service.BudgetService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportWeeklyGenerator {

    private final BudgetService budgetService;
    private final UserClient userClient;
    private final TransactionClient transactionClient;

    // Запуск каждое воскресенье в полночь
    @Scheduled(cron = "0 */2 * * * *")
    public void generateWeeklyReport() {
        addReport();
        // Логика генерации отчета
        System.out.println("Генерация еженедельного отчета...");
        // Здесь можешь собирать данные и отправлять их на email или сохранять в базу
    }
    public List<Long> getIds(){
        return userClient.userIds();
    }
    public Map<String,Double> weeklyReport(Long id){
        return transactionClient.getReportWeekly(id);
    }
    public void addReport() {
        List<Long> ids= getIds();
        for (Long id : ids) {
            budgetService.saveReportWeekly(weeklyReport(id),id);
        }
    }



}
