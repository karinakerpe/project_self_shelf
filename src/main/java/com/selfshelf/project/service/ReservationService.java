package com.selfshelf.project.service;


import com.selfshelf.project.exception.ReservationNotFoundException;
import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.HistoryRepository;
import com.selfshelf.project.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ReservationService {
    @Autowired
    private final ReservationRepository reservationRepository;

    @Autowired
    private final BookService bookService;

    @Autowired
    HistoryRepository historyRepository;

    public void reserveBook(BookEntity book, UserEntity user, LocalDate startDate, LocalDate endDate) {

        reservationRepository.save(new Reservation(startDate, endDate, user, book, Status.ACTIVE));
    }

    public List<Reservation> findReservationByUserId(Long userId) {
        return findAllActiveReservation().stream()
                .filter(reservation -> reservation.getUserEntity().getId().equals(userId)).collect(Collectors.toList());
    }

    public List<Reservation> findAllActiveReservation() {
        return reservationRepository.findActiveReservations(Status.ACTIVE);
    }

    public List<Reservation> findActiveReservationsByBookId(Long bookId){
        return findAllActiveReservation().stream()
                .filter(reservation -> reservation.getBookEntity().getId().equals(bookId)).collect(Collectors.toList());

    }

    public List<Reservation> findAllExpiredReservation(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findReservationsByReservationEndDateBefore(date);

        return reservations.stream().filter(reservation -> reservation.getStatus().name().equals(Status.ACTIVE.name())).collect(Collectors.toList());
    }

    public void deleteReservationById(Long reservationId) {
        Reservation reservation = reservationRepository.getById(reservationId);
        BookEntity reservedBook = reservation.getBookEntity();
        List<Reservation> reservationListForBook = reservedBook.getReservations();
        reservationListForBook.clear();
        reservedBook.setReservations(reservationListForBook);
        reservedBook.setBookStatus(Status.BOOK_AVAILABLE);
        bookService.updateBook(reservedBook);
        deleteByIdUpdatingBookStatus(reservationId);
    }

    public void deleteByIdUpdatingBookStatus(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(
                () ->new ReservationNotFoundException("Reservation not found!")
        );
        reservation.setStatus(Status.DELETED);
        History history = new History();
        history.setReservation(reservation);
        historyRepository.save(history);
        reservationRepository.save(reservation);
    }
}
