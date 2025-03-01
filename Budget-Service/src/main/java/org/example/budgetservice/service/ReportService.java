package org.example.budgetservice.service;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.TransactionClient;
import org.example.budgetservice.client.UserClient;
import org.example.budgetservice.model.Report;

import org.example.budgetservice.repository.ReportRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportService {

    private final BudgetService budgetService;
    private final ReportRepository reportRepository;
    private final UserClient userClient;
    private final TransactionClient transactionClient;

    // Запуск каждое воскресенье в полночь
    @Scheduled(cron = "0 * * * * *")
    public void generateWeeklyReport() {
        addReport();
        // Логика генерации отчета
        System.out.println("Генерация еженедельного отчета...");
        // Здесь можешь собирать данные и отправлять их на email или сохранять в базу
    }
    public List<Long> getIds(){
        return userClient.userIds();
    }
    public Map<String,Double> weekReport(Long id){
        return transactionClient.getReport(id);
    }
    public void addReport() {
        List<Long> ids= getIds();
        for (Long id : ids) {
            budgetService.saveReport(weekReport(id),id);
        }
    }
//    public List<Transaction> getTransactions(){
//        return transactionClient.getTransactions();
//    }
//    public void save(){
//        reportRepository.save()
//    }
//    public void asdf(){
//        Report report = Report.builder()
//                .category()
//                .userId()
//                .weekExpense()
//                .reportDate()
//
//                .build()
//    }
//
//    public Report asd(){
//        List<Transaction> transactions = getTransactions();
//
//    }


}
