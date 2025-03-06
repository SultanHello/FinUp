package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportDailyRepository extends JpaRepository<ReportDaily,Long> {
    ReportDaily findByReportId(Long id);
    List<ReportDaily> findByUserId(Long id);
}
