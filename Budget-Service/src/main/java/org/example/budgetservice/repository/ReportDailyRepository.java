

package org.example.budgetservice.repository;

import org.example.budgetservice.model.ReportDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReportDailyRepository extends JpaRepository<ReportDaily, Long> {

    List<ReportDaily> findByUserId(Long userId);

    List<ReportDaily> findAllByReportDate(LocalDate reportDate);

    List<ReportDaily> findAllByReportDateBetween(LocalDate startDate, LocalDate endDate);


    @Query("SELECT r FROM ReportDaily r WHERE r.userId = :userId " +
            "ORDER BY r.reportDate DESC, r.reportId DESC")
    List<ReportDaily> findTopByUserIdOrderByReportDateDesc(@Param("userId") Long userId);


    Optional<ReportDaily> findFirstByUserIdOrderByReportDateDescReportIdDesc(Long userId);


    @Query("SELECT r FROM ReportDaily r WHERE r.userId = :userId " +
            "AND r.reportDate BETWEEN :startDate AND :endDate " +
            "ORDER BY r.reportDate DESC")
    List<ReportDaily> findByUserIdAndReportDateBetween(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    boolean existsByUserIdAndReportDate(Long userId, LocalDate reportDate);


    long countByUserId(Long userId);
}