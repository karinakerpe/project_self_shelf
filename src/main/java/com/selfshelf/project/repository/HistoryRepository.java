package com.selfshelf.project.repository;

import com.selfshelf.project.model.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {

    @Query("Select h " + "FROM History h " + "Where h.issuedBook IS NOT NULL "
    )
    List<History> findAllIssues();
}
