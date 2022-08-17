package com.selfshelf.project.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class IssuedBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate issueStartDate;
    private LocalDate issueEndDate;
    private Status status;
    @ManyToOne
    @JoinColumn(name="user_entity_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name="book_entity_id")
    private BookEntity bookEntity;

    @OneToMany(
            mappedBy = "issuedBook",
            fetch = FetchType.LAZY
    )
    private List<History> historiesList;

    public IssuedBook(LocalDate issueStartDate, LocalDate issueEndDate, UserEntity user, BookEntity book, Status status) {
        this.issueStartDate=issueStartDate;
        this.issueEndDate=issueEndDate;
        this.userEntity=user;
        this.bookEntity=book;
        this.status=status;
    }

    public IssuedBook() {
    }
}
