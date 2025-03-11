package org.example.budgetservice.factory;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.AiClient;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.service.ReportAggregator;
import org.example.budgetservice.service.ReportDailyService;
import org.example.budgetservice.service.ReportWeeklyService;
import org.springframework.stereotype.Service;

import java.lang.ref.PhantomReference;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ReportFactory {
    private final ReportAggregator reportAggregator;
    private final AiClient aiClient;
    public ReportWeekly createWeeklyReport(Map<String, Double> info, Long userId) {

        return ReportWeekly.builder()
                .userId(userId)
                .info(info)
                .advice(aiClient.getAdvice(createRequestWeekly(info)))
                .reportDate(LocalDate.now())
                .build();
    }
    public ReportDaily createDailyReport(Map<String, Double> info, Long userId) {
        System.out.println("request :::: "+createRequestDaily(info));
        System.out.println("response :::: " +aiClient.getAdvice(createRequestDaily(info)));
        return ReportDaily.builder()
                .userId(userId)
                .info(info)
                .advice(aiClient.getAdvice(createRequestDaily(info)))
                .reportDate(LocalDate.now())
                .build();
    }
    public String createRequestDaily(Map<String,Double> info){
        return  "Today report : "+info.toString()+",this week daily reports  :  "+reportAggregator.reportsDailyWeek();
    }
    public String createRequestWeekly(Map<String,Double> info){
        return  "Week report : "+info.toString()+",this week dayly reports  :  "+reportAggregator.reportsDailyWeek();
    }
}