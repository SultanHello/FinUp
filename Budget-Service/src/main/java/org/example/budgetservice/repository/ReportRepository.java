package org.example.budgetservice.repository;

import org.example.budgetservice.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report,Long> {
    Report findByReportId(Long id);
    List<Report> findByUserId(Long id);

}
