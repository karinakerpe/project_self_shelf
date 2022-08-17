package com.selfshelf.project.service;

import com.selfshelf.project.model.BookEntity;
import com.selfshelf.project.model.Status;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BookServiceTest {
    private ArrayList<BookEntity> bookEntities = new ArrayList<>();
    private BookEntity book1 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
    private BookEntity book5 = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
    private BookEntity book2 = new BookEntity("The Emperor's New Mind", "Roger Penrose", 1989, 480, "0192861980", Status.BOOK_ISSUED);
    private BookEntity book3 = new BookEntity("The Little Mermaid", "Hans Christian Andersen", 2019, 64, "1782692509", Status.BOOK_RESERVED);
    private BookEntity book4 = new BookEntity("Test book", "Test Author", 2022, 3, "123456789", Status.DELETED);

    @MockBean
    private BookRepository bookRepository;
    @Autowired
    private BookService bookService;

    @Test
    void getAllBooks() {
        bookEntities.add(book2);
        bookEntities.add(book1);
        when(this.bookRepository.findAll()).thenReturn(bookEntities);
        assertEquals(2, this.bookService.getAllBooks().size());
    }

    @Test
    void getAllAvailableBooks() {
        bookEntities.add(book1);
        bookEntities.add(book4);
        when(this.bookRepository.findAll()).thenReturn(bookEntities);
        assertEquals(1, this.bookService.getAllAvailableBooks().size());
    }

    @Test
    void saveBook() {

        when(this.bookRepository.save((BookEntity) any())).thenReturn(book1);
        this.bookService.saveBook(book1);
        assertEquals(Status.BOOK_AVAILABLE, book1.getBookStatus());


    }
    @Test
    void saveBookList() {

        when(this.bookRepository.save((BookEntity) any())).thenReturn(book1);
        this.bookService.saveBook(book1);
        assertEquals(Status.BOOK_AVAILABLE, book1.getBookStatus());


    }

    @Test
    void saveBookError() {

        when(this.bookRepository.save((BookEntity) any())).thenThrow(new RuntimeException("Error!"));
        assertThrows(RuntimeException.class, () -> this.bookService.saveBook(book1));
        verify(this.bookRepository).save((BookEntity) any());
    }

        @Test
    void getBookById() {
        book1.setId(5L);
        Optional<BookEntity> optResult = Optional.of(book1);
        when(this.bookRepository.findById((Long) any())).thenReturn(optResult);
        assertSame(book1, this.bookService.getBookById(5L));
        verify(this.bookRepository).findById((Long)any());
    }
    @Test
    void getBookByIdEmpty() {
        when(this.bookRepository.findById((Long) any())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,()-> this.bookService.getBookById(5L));
        verify(this.bookRepository).findById((Long)any());
    }

    @Test
    void getBookByIdError() {
        when(this.bookRepository.findById((Long) any())).thenThrow(new RuntimeException("Error!"));
        assertThrows(RuntimeException.class,()-> this.bookService.getBookById(5L));
        verify(this.bookRepository).findById((Long)any());
    }



    @Test
    void getBookByIdNotDeleted() {
        book1.setId(5L);
        Optional<BookEntity> optResult = Optional.of(book1);
        when(this.bookRepository.findById((Long) any())).thenReturn(optResult);
        assertSame(book1, this.bookService.getBookByIdNotDeleted(5L));
        verify(this.bookRepository).findById((Long)any());

    }

//    @Test
//    void getBookByIdNotDeletedFail() {
//        book5.setId(5L);
//        Optional<BookEntity> optResult = Optional.of(book5);
//        when(this.bookRepository.findById((Long) any())).thenReturn(optResult);
//        assertNotSame(book5,this.bookService.getBookByIdNotDeleted(5L));
//    }

    @Test
    void updateBook() {
        book1.setId(1L);
        when(this.bookRepository.save((BookEntity) any())).thenReturn(book1);
        BookEntity booktest = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
        booktest.setId(5L);
       assertSame(book1, this.bookService.updateBook(booktest));
       verify(this.bookRepository).save((BookEntity)any());



    }

    @Test
    void deleteBookById() {
        Optional<BookEntity>optResult=Optional.of(book1);
        BookEntity testBook = new BookEntity("Alice's Adventures in Wonderland", "Lewis Carroll", 1998, 200, "019283374X", Status.BOOK_AVAILABLE);
        testBook.setId(5L);
        when(this.bookRepository.save((BookEntity) any())).thenReturn(testBook);
        when(this.bookRepository.findById((Long) any())).thenReturn(optResult);
        this.bookService.deleteBookById(5L);
        verify(this.bookRepository).save((BookEntity)any());
        verify(this.bookRepository).findById((Long)any());

    }


    @Test
    void search() {
    }
}