package com.selfshelf.project.repository;

import com.selfshelf.project.model.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;


@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {

}
