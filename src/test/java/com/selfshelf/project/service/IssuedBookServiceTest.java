package com.selfshelf.project.service;

import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.HistoryRepository;
import com.selfshelf.project.repository.IssuedBookRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class IssuedBookServiceTest {
    private ArrayList<IssuedBook> issuedBookList = new ArrayList<>();
    private BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);

    private IssuedBook issuedBook = new IssuedBook();
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

        issuedBook.setBookEntity(book1);
        issuedBook.setHistoriesList(new ArrayList<>());
        issuedBook.setId(1L);
        issuedBook.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook.setStatus(Status.ACTIVE);
        issuedBook.setUserEntity(userEntity2);

        issuedBookList.add(issuedBook);


    }

    @After
    public void clearSetUp() {
        userEntity2.setId(4L);
        issuedBook.setBookEntity(book1);
        issuedBook.setStatus(Status.ACTIVE);
        issuedBookList.clear();

    }

    @MockBean
    private BookService bookService;

    @MockBean
    private HistoryRepository historyRepository;

    @MockBean
    private IssuedBookRepository issuedBookRepository;

    @Autowired
    private IssuedBookService issuedBookService;


    @Test
    void showAllIssuedBooks() {

        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertEquals(1, this.issuedBookService.showAllIssuedBooks().size());
        verify(this.issuedBookRepository).findAll();
    }


    @Test
    void findIssuedBooksByUserIdActive() {
        when(this.issuedBookRepository.findAllByUserEntityIdEquals((Long) any())).thenReturn(issuedBookList);
        assertEquals(1, this.issuedBookService.findIssuedBooksByUserIdActive(4L).size());
        verify(this.issuedBookRepository).findAllByUserEntityIdEquals((Long) any());
    }


    @Test
    void findIssuedBooksByUserIdActiveStatusTest() {
        issuedBook.setStatus(Status.DELETED);
        issuedBookList.clear();
        issuedBookList.add(issuedBook);
        when(this.issuedBookRepository.findAllByUserEntityIdEquals((Long) any())).thenReturn(issuedBookList);
        assertTrue(this.issuedBookService.findIssuedBooksByUserIdActive(4L).isEmpty());
        verify(this.issuedBookRepository).findAllByUserEntityIdEquals((Long) any());
    }



    @Test
    void IssueBookWithActiveReservation() {

        when(this.issuedBookRepository.save((IssuedBook) any())).thenReturn(issuedBook);

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setAuthor("JaneDoe");
        bookEntity1.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity1.setId(123L);
        bookEntity1.setIsbn("Isbn");
        bookEntity1.setIssuedBooks(new ArrayList<>());
        bookEntity1.setPages(1);
        bookEntity1.setReservations(new ArrayList<>());
        bookEntity1.setTitle("Dr");
        bookEntity1.setYearOfPublish(1);
        when(this.bookService.updateBook((BookEntity) any())).thenReturn(bookEntity1);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setAuthor("JaneDoe");
        bookEntity2.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity2.setId(123L);
        bookEntity2.setIsbn("Isbn");
        bookEntity2.setIssuedBooks(new ArrayList<>());
        bookEntity2.setPages(1);
        bookEntity2.setReservations(new ArrayList<>());
        bookEntity2.setTitle("Dr");
        bookEntity2.setYearOfPublish(1);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setIssuedBooks(new ArrayList<>());
        userEntity1.setLastName("Doe");
        userEntity1.setPassword("iloveyou");
        userEntity1.setReservations(new ArrayList<>());
        userEntity1.setUserRole(UserRole.ADMIN);

        this.issuedBookService.issueBookWithActiveReservation(bookEntity2, userEntity1);
        verify(this.issuedBookRepository).save((IssuedBook) any());
        verify(this.bookService).updateBook((BookEntity) any());
        assertEquals(Status.BOOK_ISSUED, bookEntity2.getBookStatus());
    }


    @Test
    void FindIssueBooksWithIssueStatusActive() {
        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertEquals(1, this.issuedBookService.findIssueBooksWithIssueStatusActive().size());
        verify(this.issuedBookRepository).findAll();
    }


    @Test
    void FindIssueBooksWithIssueStatusActiveStatusTest() {

        IssuedBook issuedBook = new IssuedBook();
        issuedBook.setBookEntity(book1);
        issuedBook.setHistoriesList(new ArrayList<>());
        issuedBook.setId(123L);
        issuedBook.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook.setStatus(Status.DELETED);
        issuedBook.setUserEntity(userEntity2);

        ArrayList<IssuedBook> issuedBookList = new ArrayList<>();
        issuedBookList.add(issuedBook);
        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertTrue(this.issuedBookService.findIssueBooksWithIssueStatusActive().isEmpty());
        verify(this.issuedBookRepository).findAll();
    }


    @Test
    void FindIssueBooksByBookIdWithIssueStatusActive() {
        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertTrue(this.issuedBookService.findIssueBooksByBookIdWithIssueStatusActive(123L).isEmpty());
        verify(this.issuedBookRepository).findAll();
    }


    @Test
    void testFindIssueBooksByBookIdWithIssueStatusActive2() {

        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertEquals(1, this.issuedBookService.findIssueBooksByBookIdWithIssueStatusActive(1L).size());
        verify(this.issuedBookRepository).findAll();
    }


    @Test
    void findIssueBooksByBookIdWithIssueStatusActive3() {

        issuedBookList.clear();
        issuedBook.setStatus(Status.DELETED);
        issuedBookList.add(issuedBook);

        when(this.issuedBookRepository.findAll()).thenReturn(issuedBookList);
        assertTrue(this.issuedBookService.findIssueBooksByBookIdWithIssueStatusActive(1L).isEmpty());
        verify(this.issuedBookRepository).findAll();
    }




    @Test
    void getIssuedBookById() {

        when(this.issuedBookRepository.getById((Long) any())).thenReturn(issuedBook);
        assertSame(issuedBook, this.issuedBookService.getIssuedBookById(3L));
        verify(this.issuedBookRepository).getById((Long) any());
    }


    @Test
    void ReturnBook() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setAuthor("JaneDoe");
        bookEntity.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity.setId(123L);
        bookEntity.setIsbn("Isbn");
        bookEntity.setIssuedBooks(new ArrayList<>());
        bookEntity.setPages(1);
        bookEntity.setReservations(new ArrayList<>());
        bookEntity.setTitle("Dr");
        bookEntity.setYearOfPublish(1);

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setFirstName("Jane");
        userEntity.setId(123L);
        userEntity.setIssuedBooks(new ArrayList<>());
        userEntity.setLastName("Doe");
        userEntity.setPassword("iloveyou");
        userEntity.setReservations(new ArrayList<>());
        userEntity.setUserRole(UserRole.ADMIN);

        IssuedBook issuedBook = new IssuedBook();
        issuedBook.setBookEntity(bookEntity);
        issuedBook.setHistoriesList(new ArrayList<>());
        issuedBook.setId(123L);
        issuedBook.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook.setStatus(Status.BOOK_AVAILABLE);
        issuedBook.setUserEntity(userEntity);

        BookEntity bookEntity1 = new BookEntity();
        bookEntity1.setAuthor("JaneDoe");
        bookEntity1.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity1.setId(123L);
        bookEntity1.setIsbn("Isbn");
        bookEntity1.setIssuedBooks(new ArrayList<>());
        bookEntity1.setPages(1);
        bookEntity1.setReservations(new ArrayList<>());
        bookEntity1.setTitle("Dr");
        bookEntity1.setYearOfPublish(1);

        UserEntity userEntity1 = new UserEntity();
        userEntity1.setEmail("jane.doe@example.org");
        userEntity1.setFirstName("Jane");
        userEntity1.setId(123L);
        userEntity1.setIssuedBooks(new ArrayList<>());
        userEntity1.setLastName("Doe");
        userEntity1.setPassword("iloveyou");
        userEntity1.setReservations(new ArrayList<>());
        userEntity1.setUserRole(UserRole.ADMIN);

        IssuedBook issuedBook1 = new IssuedBook();
        issuedBook1.setBookEntity(bookEntity1);
        issuedBook1.setHistoriesList(new ArrayList<>());
        issuedBook1.setId(123L);
        issuedBook1.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook1.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook1.setStatus(Status.BOOK_AVAILABLE);
        issuedBook1.setUserEntity(userEntity1);
        when(this.issuedBookRepository.save((IssuedBook) any())).thenReturn(issuedBook1);
        when(this.issuedBookRepository.getById((Long) any())).thenReturn(issuedBook);

        BookEntity bookEntity2 = new BookEntity();
        bookEntity2.setAuthor("JaneDoe");
        bookEntity2.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity2.setId(123L);
        bookEntity2.setIsbn("Isbn");
        bookEntity2.setIssuedBooks(new ArrayList<>());
        bookEntity2.setPages(1);
        bookEntity2.setReservations(new ArrayList<>());
        bookEntity2.setTitle("Dr");
        bookEntity2.setYearOfPublish(1);

        UserEntity userEntity2 = new UserEntity();
        userEntity2.setEmail("jane.doe@example.org");
        userEntity2.setFirstName("Jane");
        userEntity2.setId(123L);
        userEntity2.setIssuedBooks(new ArrayList<>());
        userEntity2.setLastName("Doe");
        userEntity2.setPassword("iloveyou");
        userEntity2.setReservations(new ArrayList<>());
        userEntity2.setUserRole(UserRole.ADMIN);

        IssuedBook issuedBook2 = new IssuedBook();
        issuedBook2.setBookEntity(bookEntity2);
        issuedBook2.setHistoriesList(new ArrayList<>());
        issuedBook2.setId(123L);
        issuedBook2.setIssueEndDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setIssueStartDate(LocalDate.ofEpochDay(1L));
        issuedBook2.setStatus(Status.BOOK_AVAILABLE);
        issuedBook2.setUserEntity(userEntity2);

        BookEntity bookEntity3 = new BookEntity();
        bookEntity3.setAuthor("JaneDoe");
        bookEntity3.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity3.setId(123L);
        bookEntity3.setIsbn("Isbn");
        bookEntity3.setIssuedBooks(new ArrayList<>());
        bookEntity3.setPages(1);
        bookEntity3.setReservations(new ArrayList<>());
        bookEntity3.setTitle("Dr");
        bookEntity3.setYearOfPublish(1);

        UserEntity userEntity3 = new UserEntity();
        userEntity3.setEmail("jane.doe@example.org");
        userEntity3.setFirstName("Jane");
        userEntity3.setId(123L);
        userEntity3.setIssuedBooks(new ArrayList<>());
        userEntity3.setLastName("Doe");
        userEntity3.setPassword("iloveyou");
        userEntity3.setReservations(new ArrayList<>());
        userEntity3.setUserRole(UserRole.ADMIN);

        Reservation reservation = new Reservation();
        reservation.setBookEntity(bookEntity3);
        reservation.setHistoriesList(new ArrayList<>());
        reservation.setId(123L);
        reservation.setReservationEndDate(LocalDate.ofEpochDay(1L));
        reservation.setReservationStartDate(LocalDate.ofEpochDay(1L));
        reservation.setStatus(Status.BOOK_AVAILABLE);
        reservation.setUserEntity(userEntity3);

        History history = new History();
        history.setAcctualreturnDate(LocalDate.ofEpochDay(1L));
        history.setId(123L);
        history.setIssuedBook(issuedBook2);
        history.setOverdueDays(1);
        history.setReservation(reservation);
        when(this.historyRepository.save((History) any())).thenReturn(history);

        BookEntity bookEntity4 = new BookEntity();
        bookEntity4.setAuthor("JaneDoe");
        bookEntity4.setBookStatus(Status.BOOK_AVAILABLE);
        bookEntity4.setId(123L);
        bookEntity4.setIsbn("Isbn");
        bookEntity4.setIssuedBooks(new ArrayList<>());
        bookEntity4.setPages(1);
        bookEntity4.setReservations(new ArrayList<>());
        bookEntity4.setTitle("Dr");
        bookEntity4.setYearOfPublish(1);
        when(this.bookService.updateBook((BookEntity) any())).thenReturn(bookEntity4);
        this.issuedBookService.returnBook(123L, LocalDate.ofEpochDay(1L));
        verify(this.issuedBookRepository).getById((Long) any());
        verify(this.issuedBookRepository).save((IssuedBook) any());
        verify(this.historyRepository).save((History) any());
        verify(this.bookService).updateBook((BookEntity) any());
    }

}