package org.example.budgetservice.generator;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;

import org.example.budgetservice.repository.ReportWeeklyRepository;
import org.example.budgetservice.service.BudgetService;
import org.example.budgetservice.service.ReportDailyService;
import org.example.budgetservice.service.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportDailyGenerator implements ReportGenerator{

    private final BudgetService budgetService;
    private final ReportWeeklyRepository reportWeeklyRepository;
    private final UserClient userClient;
    private final ReportDailyService reportDailyService;
    private final TransactionClient transactionClient;

    // Запуск каждое воскресенье в полночь
    @Scheduled(cron = "0 5 0 * * *")
    public void generateReport() {
        addReport();
        // Логика генерации отчета
        System.out.println("Генерация еженедневного отчета...");
        // Здесь можешь собирать данные и отправлять их на email или сохранять в базу
    }

    public void addReport() {
        System.out.println("00000000000...");

        List<Long> ids = reportDailyService.getAllUserIds();  // Сервис получает пользователей

        for (Long id : ids) {
            System.out.println("11111111111..."+id);
            reportDailyService.saveReportForUser(id);


        }
    }



}