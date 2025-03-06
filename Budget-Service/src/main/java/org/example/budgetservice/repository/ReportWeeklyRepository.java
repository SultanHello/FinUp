package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportWeekly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportWeeklyRepository extends JpaRepository<ReportWeekly,Long> {
    ReportWeekly findByReportId(Long id);
    List<ReportWeekly> findByUserId(Long id);

}
