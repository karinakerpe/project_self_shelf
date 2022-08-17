package com.selfshelf.project.service;

import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.HistoryRepository;
import com.selfshelf.project.repository.ReservationRepository;
import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.selfshelf.project.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class ReservationServiceTest {
    private ArrayList<Reservation> reservedBookList = new ArrayList<>();
    private BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);

    private Reservation reservation = new Reservation();
    private UserEntity userEntity2 = new UserEntity();

    @BeforeEach
    public void setUp() {
        book1.setId(1L);
        userEntity2.setEmail("cits@email.lv");
        userEntity2.setFirstName("Tests");
        userEntity2.setId(4L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Testere");
        userEntity2.setPassword("password");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.USER);

        reservation.setBookEntity(book1);
        reservation.setHistoriesList(new ArrayList<>());
        reservation.setId(123L);
        reservation.setReservationEndDate(LocalDate.ofEpochDay(1L));
        reservation.setReservationStartDate(LocalDate.ofEpochDay(1L));
        reservation.setStatus(Status.ACTIVE);
        reservation.setUserEntity(userEntity2);

        reservedBookList.add(reservation);


    }

    @After
    public void clearSetUp() {
        userEntity2.setId(4L);
        reservation.setBookEntity(book1);
        reservation.setStatus(Status.ACTIVE);
        reservedBookList.clear();

    }
    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationService reservationService;
@MockBean
    private BookService bookService;
    @MockBean
    private HistoryRepository historyRepository;

    @Test
    void reserveBook() {
        this.reservationService.reserveBook(book1,userEntity2,LocalDate.ofEpochDay(1L),LocalDate.ofEpochDay(1L));
        verify(this.reservationRepository).save((Reservation) any());
    }

    @Test
    void findReservationByUserId() {
        when(this.reservationRepository.findActiveReservations((com.selfshelf.project.model.Status) any()))
                .thenReturn(reservedBookList);
        assertEquals(1,this.reservationService.findReservationByUserId(4L).size());
        verify(this.reservationRepository).findActiveReservations((com.selfshelf.project.model.Status) any());
    }

    @Test
    void findReservationByUserIdEmpty() {
        when(this.reservationRepository.findActiveReservations((com.selfshelf.project.model.Status) any()))
                .thenReturn(reservedBookList);
        assertTrue(this.reservationService.findReservationByUserId(476L).isEmpty());
        verify(this.reservationRepository).findActiveReservations((com.selfshelf.project.model.Status) any());
    }

    @Test
    void findAllActiveReservation() {
        Reservation reservation1 = new Reservation();
        reservation1.setBookEntity(book1);
        reservation1.setHistoriesList(new ArrayList<>());
        reservation1.setId(123L);
        reservation1.setReservationEndDate(LocalDate.ofEpochDay(1L));
        reservation1.setReservationStartDate(LocalDate.ofEpochDay(1L));
        reservation1.setStatus(Status.ACTIVE);
        reservation1.setUserEntity(userEntity2);
        reservedBookList.add(reservation1);
        when(this.reservationRepository.findActiveReservations((com.selfshelf.project.model.Status) any()))
                .thenReturn(reservedBookList);
        assertEquals(2, this.reservationService.findAllActiveReservation().size());
        verify(this.reservationRepository).findActiveReservations((com.selfshelf.project.model.Status) any());
    }

    @Test
    void findActiveReservationsByBookId() {
        when(this.reservationRepository.findActiveReservations((com.selfshelf.project.model.Status) any()))
                .thenReturn(reservedBookList);
        assertEquals(1, this.reservationService.findActiveReservationsByBookId(1L).size());
        verify(this.reservationRepository).findActiveReservations((com.selfshelf.project.model.Status) any());
    }

    @Test
    void findActiveReservationsByBookIdEmpty() {
        when(this.reservationRepository.findActiveReservations((com.selfshelf.project.model.Status) any()))
                .thenReturn(reservedBookList);
        assertFalse( this.reservationService.findActiveReservationsByBookId(1L).isEmpty());
        verify(this.reservationRepository).findActiveReservations((com.selfshelf.project.model.Status) any());
    }

    @Test
    void findAllExpiredReservation() {
        when(this.reservationRepository.findReservationsByReservationEndDateBefore((LocalDate) any()))
                .thenReturn(new ArrayList<>());
        assertTrue(this.reservationService.findAllExpiredReservation(LocalDate.ofEpochDay(1L)).isEmpty());
        verify(this.reservationRepository).findReservationsByReservationEndDateBefore((LocalDate) any());
    }

    @Test
    void deleteReservationById() {
//        when(this.bookService.updateBook((BookEntity) any())).thenReturn(book1);
//        this.reservationService.deleteReservationById(123L);
//        verify(this.reservationRepository).getById((Long) any());
//        verify(this.reservationRepository).save((Reservation) any());
//        verify(this.reservationRepository).findById((Long) any());
//        verify(this.historyRepository).save((History) any());
//        verify(this.bookService).updateBook((BookEntity) any());
        //TODO-test
    }

    @Test
    void deleteByIdUpdatingBookStatus() {
        //TODO-test
    }
}