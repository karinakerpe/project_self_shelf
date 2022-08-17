package com.selfshelf.project.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table
@Data
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate acctualreturnDate;
    private int overdueDays;
    @ManyToOne
    @JoinColumn(name="issued_book_id")
    private IssuedBook issuedBook;
    @ManyToOne
    @JoinColumn(name="reservation_id")
    private Reservation reservation;
}
