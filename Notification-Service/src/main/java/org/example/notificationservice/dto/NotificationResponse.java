package org.example.notificationservice.dto;


import lombok.Data;

@Data
public class NotificationResponse {
    String to;
    String subject;
    String text;
}
