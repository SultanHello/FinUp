package org.example.budgetservice.controller;


import lombok.AllArgsConstructor;
import org.example.budgetservice.model.Report;
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

    @GetMapping("/report")
    public List<Report> getReport(){
        return budgetService.getReports();
    }

    @GetMapping("/report/{reportId}")
    public Report getReportById(@PathVariable Long reportId){
        return budgetService.getReportByReportId(reportId);
    }
    @GetMapping("/report/user/{userId}")
    public List<Report> getReportByUserId(@PathVariable Long userId){
        return budgetService.getReportByUserId(userId);
    }


    @GetMapping("/report/last/user/{userId}")
    public Report getLastReport(@PathVariable Long userId){
        return budgetService.getLastReport(userId);
    }



}
