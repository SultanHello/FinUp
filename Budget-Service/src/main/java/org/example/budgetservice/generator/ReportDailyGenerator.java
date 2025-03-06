package org.example.budgetservice.generator;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;

import org.example.budgetservice.repository.ReportWeeklyRepository;
import org.example.budgetservice.service.BudgetService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportDailyGenerator {

    private final BudgetService budgetService;
    private final ReportWeeklyRepository reportWeeklyRepository;
    private final UserClient userClient;
    private final TransactionClient transactionClient;

    // Запуск каждое воскресенье в полночь
    @Scheduled(cron = "*/17 * * * * *")
    public void generateDailyReport() {
        addReport();
        // Логика генерации отчета
        System.out.println("Генерация еженедневного отчета...");
        // Здесь можешь собирать данные и отправлять их на email или сохранять в базу
    }
    public List<Long> getIds(){
        return userClient.userIds();
    }
    public Map<String,Double> dailyReport(Long id){
        return transactionClient.getReportDaily(id);
    }
    public void addReport() {
        System.out.println("00000000000...");
        List<Long> ids= getIds();
        for (Long id : ids) {
            System.out.println("11111111111..."+id);
            budgetService.saveReportDaily(dailyReport(id),id);
        }
    }



}