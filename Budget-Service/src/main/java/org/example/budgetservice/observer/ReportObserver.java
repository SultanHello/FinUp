package org.example.budgetservice.observer;

import org.example.budgetservice.model.Report;

public interface ReportObserver {
    void onReportCreated(Report report);
}
