package org.example.budgetservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
@Entity
@Table(name = "reports_daily")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "advice")
public class ReportDaily implements Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long userId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "daily_info_data", joinColumns = @JoinColumn(name = "report_id"))
    @MapKeyColumn(name = "metric")
    @Column(name = "value")
    private Map<String,Double> info;
    @Column(length = 10000)  // Или больше, если нужно
    private String advice;

    private LocalDate reportDate;
}
