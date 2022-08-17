package com.selfshelf.project.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Table(name="book_entity")
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String author;

    private Integer yearOfPublish;

    private Integer pages;

    private Status bookStatus;

    private String isbn;

    @OneToMany(
            mappedBy = "bookEntity",
            fetch = FetchType.LAZY
    )
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(
            mappedBy = "bookEntity",
            fetch = FetchType.LAZY
    )
    private List<IssuedBook> issuedBooks = new ArrayList<>();


    public BookEntity(String title, String author, int yearOfPublish, int pages, String isbn, Status status) {
        this.title=title;
        this.author=author;
        this.yearOfPublish=yearOfPublish;
        this.pages=pages;
        this.isbn=isbn;
        this.bookStatus=status;
    }
}
