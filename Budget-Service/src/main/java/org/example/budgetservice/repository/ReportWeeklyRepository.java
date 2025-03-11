package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReportWeeklyRepository extends JpaRepository<ReportWeekly,Long> {
    ReportWeekly findByReportId(Long id);
    List<ReportWeekly> findByUserId(Long id);



}
