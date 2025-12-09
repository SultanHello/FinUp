package org.example.aiservice.repository;

import org.example.aiservice.connection.ContentText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContentTextRepository extends JpaRepository<ContentText, Long> {

    /**
     * ✅ ИСПРАВЛЕНО: Теперь учитывает type для разделения контекстов
     */
    List<ContentText> findByType(String type);

    /**
     * ✅ НОВЫЙ МЕТОД: Последние N сообщений для контекста
     */
    @Query("SELECT c FROM ContentText c WHERE c.type = :type " +
            "ORDER BY c.id DESC")
    List<ContentText> findTopNByTypeOrderByIdDesc(
            @Param("type") String type
    );

    /**
     * ✅ НОВЫЙ МЕТОД: Очистка старых контекстов
     */
    void deleteByType(String type);

    /**
     * ✅ НОВЫЙ МЕТОД: Подсчёт сообщений по типу
     */
    long countByType(String type);
}