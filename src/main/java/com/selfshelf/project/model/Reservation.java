package com.selfshelf.project.model;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;
    private Status status;

    @ManyToOne
    @JoinColumn(name="user_entity_id")
    private UserEntity userEntity;


    @ManyToOne
    @JoinColumn(name="book_entity_id")
    private BookEntity bookEntity;

    @OneToMany(
            mappedBy = "reservation",
            fetch = FetchType.LAZY
    )
    private List<History> historiesList;


    public Reservation(LocalDate startDate, LocalDate endDate, UserEntity user, BookEntity book, Status status) {
        this.reservationStartDate=startDate;
        this.reservationEndDate=endDate;
        this.userEntity=user;
        this.bookEntity=book;
        this.status=status;
    }

    public Reservation() {

    }
}
