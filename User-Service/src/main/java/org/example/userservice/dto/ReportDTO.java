package org.example.userservice.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Map;


@Data
public class ReportDTO {
    private Long reportId;

    private Long userId;


    private Map<String,Double> info;
    private String advice;

    private LocalDate reportDate;




}
