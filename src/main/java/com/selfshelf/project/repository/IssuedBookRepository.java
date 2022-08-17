package com.selfshelf.project.repository;

import com.selfshelf.project.model.IssuedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook,Long> {
    List<IssuedBook> findAllByUserEntityIdEquals(Long userId);
    List<IssuedBook> findIssuedBookByBookEntityIdEqualsOrderByIssueEndDateAsc(Long bookId);
}
