package org.example.budgetservice.notification;

import lombok.AllArgsConstructor;
import org.example.budgetservice.client.AuthClient;
import org.example.budgetservice.dto.NotificationDTO;
import org.example.budgetservice.model.Report;
import org.example.budgetservice.model.ReportWeekly;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class NotificationSender {
    private final AuthClient authClient;
    private KafkaTemplate<String, NotificationDTO> kafkaTemplate;
    public void send(Report report) {
        NotificationDTO notificationDTO = NotificationDTO.builder()
                .to(authClient.getUsername(report.getUserId()))
                .subject("report "+ report.getReportDate())
                .text(report.getInfo().toString())
                .build();
        kafkaTemplate.send("notification",notificationDTO);
    }
}
