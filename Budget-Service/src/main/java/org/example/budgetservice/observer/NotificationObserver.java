package org.example.budgetservice.observer;

import lombok.AllArgsConstructor;
import org.example.budgetservice.model.Report;
import org.example.budgetservice.notification.NotificationSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class NotificationObserver implements ReportObserver {
    private final NotificationSender sender;

    @Override
    public void onReportCreated(Report report) {
        sender.send(report);
    }


}