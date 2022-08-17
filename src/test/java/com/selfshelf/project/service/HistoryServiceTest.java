package com.selfshelf.project.service;

import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.BookRepository;
import com.selfshelf.project.repository.HistoryRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class HistoryServiceTest {


    private BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
    private BookEntity book5 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
    private UserEntity userEntity = new UserEntity();
    private UserEntity userEntity2 = new UserEntity();
    private IssuedBook issuedBook = new IssuedBook();
    private IssuedBook issuedBook2 = new IssuedBook();
    private IssuedBook issuedBook3 = new IssuedBook();
    private Reservation reservation = new Reservation();
    @MockBean
    private HistoryRepository historyRepository;

    @Autowired
    private HistoryService historyService;

    @Test
    void testFindIssueHistoryByUserIdEmpty() {
        when(this.historyRepository.findAllIssues()).thenReturn(new ArrayList<>());
        assertTrue(this.historyService.findIssueHistoryByUserId(5L).isEmpty());
        verify(this.historyRepository).findAllIssues();
    }

    @Test
    void findIssueHistoryByUserId() {
        userEntity.setEmail("email@email.lv");
        userEntity.setFirstName("Karina");
        userEntity.setId(3L);
        userEntity.setIssuedBooks(new ArrayList<>());
        userEntity.setLastName("Testere");
        userEntity.setPassword("password");
        userEntity.setReservations(new ArrayList<>());
        userEntity.setUserRole(UserRole.ADMIN);

        userEntity2.setEmail("cits@email.lv");
        userEntity2.setFirstName("Tests");
        userEntity2.setId(4L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Testere");
        userEntity2.setPassword("password");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.USER);


        issuedBook.setBookEntity(book1);
        issuedBook.setHistoriesList(new ArrayList<>());
        issuedBook.setId(1L);
        issuedBook.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook.setStatus(Status.ACTIVE);
        issuedBook.setUserEntity(userEntity);

        issuedBook2.setBookEntity(book1);
        issuedBook2.setHistoriesList(new ArrayList<>());
        issuedBook2.setId(2L);
        issuedBook2.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setStatus(Status.ACTIVE);
        issuedBook2.setUserEntity(userEntity);

        issuedBook3.setBookEntity(book5);
        issuedBook3.setHistoriesList(new ArrayList<>());
        issuedBook3.setId(3L);
        issuedBook3.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook3.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook3.setStatus(Status.ACTIVE);
        issuedBook3.setUserEntity(userEntity2);

        reservation.setBookEntity(book1);
        reservation.setHistoriesList(new ArrayList<>());
        reservation.setId(123L);
        reservation.setReservationEndDate(LocalDate.ofEpochDay(1L));
        reservation.setReservationStartDate(LocalDate.ofEpochDay(1L));
        reservation.setStatus(Status.DELETED);
        reservation.setUserEntity(userEntity);

        History history = new History();
        history.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history.setId(1L);
        history.setIssuedBook(issuedBook);
        history.setOverdueDays(1);
        history.setReservation(reservation);

        History history2 = new History();
        history2.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history2.setId(2L);
        history2.setIssuedBook(issuedBook2);
        history2.setOverdueDays(10);
        history2.setReservation(reservation);

        History history3 = new History();
        history3.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history3.setId(3L);
        history3.setIssuedBook(issuedBook3);
        history3.setOverdueDays(10);
        history3.setReservation(null);

        ArrayList<History> historyList = new ArrayList<>();
        historyList.add(history);
        historyList.add(history2);
        historyList.add(history3);
        when(this.historyRepository.findAllIssues()).thenReturn(historyList);
        assertEquals(2, this.historyService.findIssueHistoryByUserId(3L).size());
        verify(this.historyRepository).findAllIssues();
    }

    @Test
    void findIssueHistoryByBookId() {
        book1.setId(1L);
        book5.setId(2L);
        userEntity.setEmail("email@email.lv");
        userEntity.setFirstName("Karina");
        userEntity.setId(3L);
        userEntity.setIssuedBooks(new ArrayList<>());
        userEntity.setLastName("Testere");
        userEntity.setPassword("password");
        userEntity.setReservations(new ArrayList<>());
        userEntity.setUserRole(UserRole.ADMIN);

        userEntity2.setEmail("cits@email.lv");
        userEntity2.setFirstName("Tests");
        userEntity2.setId(4L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Testere");
        userEntity2.setPassword("password");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.USER);


        issuedBook.setBookEntity(book1);
        issuedBook.setHistoriesList(new ArrayList<>());
        issuedBook.setId(1L);
        issuedBook.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook.setStatus(Status.ACTIVE);
        issuedBook.setUserEntity(userEntity);

        issuedBook2.setBookEntity(book1);
        issuedBook2.setHistoriesList(new ArrayList<>());
        issuedBook2.setId(1L);
        issuedBook2.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setStatus(Status.ACTIVE);
        issuedBook2.setUserEntity(userEntity2);

        issuedBook3.setBookEntity(book5);
        issuedBook3.setHistoriesList(new ArrayList<>());
        issuedBook3.setId(1L);
        issuedBook3.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook3.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook3.setStatus(Status.ACTIVE);
        issuedBook3.setUserEntity(userEntity2);

        History history = new History();
        history.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history.setId(1L);
        history.setIssuedBook(issuedBook);
        history.setOverdueDays(1);
        history.setReservation(null);

        History history2 = new History();
        history2.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history2.setId(2L);
        history2.setIssuedBook(issuedBook2);
        history2.setOverdueDays(10);
        history2.setReservation(null);

        History history3 = new History();
        history3.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history3.setId(3L);
        history3.setIssuedBook(issuedBook3);
        history3.setOverdueDays(0);
        history3.setReservation(null);

        ArrayList<History> historyList = new ArrayList<>();
        historyList.add(history);
        historyList.add(history2);
        historyList.add(history3);

        when(this.historyRepository.findAllIssues()).thenReturn(historyList);
        assertEquals(2, this.historyService.findIssueHistoryByBookId(1L).size());
        verify(this.historyRepository).findAllIssues();

    }
}