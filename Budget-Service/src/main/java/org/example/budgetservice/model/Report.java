package org.example.budgetservice.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "reports")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long userId;

    @ElementCollection
    @CollectionTable(name = "info_data", joinColumns = @JoinColumn(name = "report_id"))
    @MapKeyColumn(name = "metric")
    @Column(name = "value")
    private Map<String,Double> info;

    private LocalDate reportDate;
}
