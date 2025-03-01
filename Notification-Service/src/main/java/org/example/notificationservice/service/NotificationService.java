package org.example.notificationservice.service;


import lombok.RequiredArgsConstructor;
import org.example.notificationservice.dto.NotificationResponse;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;

    @KafkaListener(topics = "notification", groupId = "my-group6")
    public void listenToObjectMessage(NotificationResponse notificationResponse) {
        sendEmail(notificationResponse);
    }

    public void sendEmail(NotificationResponse notificationResponse) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("asimbek06@mail.ru");
            message.setTo(notificationResponse.getTo());
            message.setSubject(notificationResponse.getSubject());
            message.setText(notificationResponse.getText());
            mailSender.send(message);
        }catch (Exception e){
            throw new RuntimeException("error with Java Mail Sender");
        }

    }

}
