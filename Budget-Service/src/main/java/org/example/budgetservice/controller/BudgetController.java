package org.example.budgetservice.controller;


import lombok.AllArgsConstructor;
import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.example.budgetservice.service.BudgetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/budget")

@AllArgsConstructor
public class BudgetController {
    private final BudgetService budgetService;

    @GetMapping("/report/weekly")
    public List<ReportWeekly> getReportWeekly(){
        return budgetService.getReportsWeekly();
    }



    @GetMapping("/report/weekly/{reportId}")
    public ReportWeekly getReportWeeklyById(@PathVariable Long reportId){
        return budgetService.getReportWeeklyByReportId(reportId);
    }
    @GetMapping("/report/weekly/user/{userId}")
    public List<ReportWeekly> getReportWeeklyByUserId(@PathVariable Long userId){
        return budgetService.getReportWeeklyByUserId(userId);
    }


    @GetMapping("/report/weekly/last/user/{userId}")
    public ReportWeekly getLastReportWeekly(@PathVariable Long userId){
        System.out.println("lohlohlholhohlhoho");
        return budgetService.getLastReportWeekly(userId);
    }


    @GetMapping("/report/daily")
    public List<ReportDaily> getReportDaily(){
        return budgetService.getReportsDaily();
    }

    @GetMapping("/report/daily/{reportId}")
    public ReportDaily getReportDailyById(@PathVariable Long reportId){
        return budgetService.getReportDailyByReportId(reportId);
    }

    @GetMapping("/report/daily/user/{userId}")
    public List<ReportDaily> getReportDailyByUserId(@PathVariable Long userId){
        return budgetService.getReportDailyByUserId(userId);
    }

    @GetMapping("/report/daily/last/user/{userId}")
    public ReportDaily getLastReportDaily(@PathVariable Long userId){
        System.out.println("last week repoert");
        return budgetService.getLastReportDaily(userId);
    }


}
