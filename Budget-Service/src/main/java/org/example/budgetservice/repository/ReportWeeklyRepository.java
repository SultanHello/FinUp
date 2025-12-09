package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportWeekly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportWeeklyRepository extends JpaRepository<ReportWeekly, Long> {

    ReportWeekly findByReportId(Long reportId);

    List<ReportWeekly> findByUserId(Long userId);


    Optional<ReportWeekly> findFirstByUserIdOrderByReportDateDescReportIdDesc(Long userId);


    @Query("SELECT r FROM ReportWeekly r WHERE r.userId = :userId " +
            "AND r.reportDate BETWEEN :startDate AND :endDate " +
            "ORDER BY r.reportDate DESC")
    List<ReportWeekly> findByUserIdAndReportDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    boolean existsByUserIdAndReportDate(Long userId, LocalDate reportDate);


    @Query("SELECT r FROM ReportWeekly r WHERE r.userId = :userId " +
            "ORDER BY r.reportDate DESC, r.reportId DESC")
    List<ReportWeekly> findTopNByUserId(@Param("userId") Long userId);
}