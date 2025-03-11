package org.example.aiservice.repository;

import org.example.aiservice.connection.ContentText;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentTextRepository extends JpaRepository<ContentText,Long> {
    List<ContentText> findByType(String type);
}
