package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportDaily;
import org.example.budgetservice.model.ReportWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReportDailyRepository extends JpaRepository<ReportDaily,Long> {
    ReportDaily findByReportId(Long id);
    List<ReportDaily> findByUserId(Long id);

    @Query("SELECT r FROM ReportDaily r WHERE r.reportDate = :date")
    List<ReportDaily> findAllByReportDate(LocalDate date);

    @Query("SELECT r FROM ReportDaily r WHERE r.reportDate BETWEEN :start AND :end")
    List<ReportDaily> findAllByReportDateBetween(LocalDate start, LocalDate end);
}
