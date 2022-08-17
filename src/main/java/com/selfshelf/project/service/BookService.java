package com.selfshelf.project.service;

import com.selfshelf.project.model.BookEntity;
import com.selfshelf.project.model.Status;
import com.selfshelf.project.model.projections.BookSearch;
import com.selfshelf.project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.matchingAny;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;


    public List<BookEntity> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<BookEntity> getAllAvailableBooks() {
        List<BookEntity> allBooks = bookRepository.findAll();
        return allBooks.stream()
                .filter(book -> book.getBookStatus().equals(Status.BOOK_AVAILABLE))
                .collect(Collectors.toList());
    }



    public BookEntity saveBook(BookEntity book) {
        book.setBookStatus(Status.BOOK_AVAILABLE);
        return bookRepository.save(book);
    }


    public BookEntity getBookById(Long id) { //todo in use
        return bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Book not found"));
    }

    public BookEntity getBookByIdNotDeleted (Long id){
        BookEntity book = getBookById(id);
        if(book.getBookStatus().equals(Status.DELETED)){
            throw new RuntimeException("Book not found");
        }
        return book;
    }


    public BookEntity updateBook(BookEntity book) {
        return bookRepository.save(book);
    }


    public void deleteBookById(Long id) {
        BookEntity book = getBookByIdNotDeleted(id);
        book.setBookStatus(Status.DELETED);
        bookRepository.save(book);
    }


    public List<BookEntity> search(BookSearch bookSearch) {
        BookEntity book = new BookEntity();
        book.setTitle(bookSearch.getTitle());
        book.setAuthor(bookSearch.getAuthor());
        book.setYearOfPublish(bookSearch.getYearOfPublish());
        book.setPages(bookSearch.getPages());

        Example<BookEntity> bookExample = Example.of(book, matchingAny().withIgnoreNullValues());
        return bookRepository.findAll(bookExample);
    }



}

