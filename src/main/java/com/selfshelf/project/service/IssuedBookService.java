package com.selfshelf.project.service;


import com.selfshelf.project.model.*;
import com.selfshelf.project.repository.HistoryRepository;
import com.selfshelf.project.repository.IssuedBookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;



@RequiredArgsConstructor
@Service
public class IssuedBookService {
    @Autowired
    private final IssuedBookRepository issuedBookRepository;
    @Autowired
    private final BookService bookService;
    @Autowired
    HistoryRepository historyRepository;


    public List<IssuedBook> showAllIssuedBooks() {
        return issuedBookRepository.findAll();
    }

    public List<IssuedBook> findIssuedBooksByUserIdActive(Long userId) {
        List<IssuedBook> books = issuedBookRepository.findAllByUserEntityIdEquals(userId);
        return books.stream()
                .filter(issuedBook -> issuedBook.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());

    }


    public void issueBookWithActiveReservation(BookEntity book, UserEntity user) {
        LocalDate issueStartDate = LocalDate.now().minusDays(30);
        LocalDate issueEndDate = issueStartDate.plusDays(21);
        book.setBookStatus(Status.BOOK_ISSUED);
        bookService.updateBook(book);
        issuedBookRepository.save(new IssuedBook(issueStartDate, issueEndDate, user, book, Status.ACTIVE));
    }


    public List<IssuedBook> findIssueBooksWithIssueStatusActive() {
        List<IssuedBook> allIssues = issuedBookRepository.findAll();
        return allIssues.stream().filter(issuedBook -> issuedBook.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList());

    }

    public List<IssuedBook> findIssueBooksByBookIdWithIssueStatusActive(Long bookId) {
        List<IssuedBook> allIssues = issuedBookRepository.findAll();
        return allIssues.stream().filter(issuedBook -> issuedBook.getStatus().equals(Status.ACTIVE))
                .filter(issuedBook -> issuedBook.getBookEntity().getId().equals(bookId))
                .collect(Collectors.toList());

    }

    public IssuedBook getIssuedBookById(Long issueId) {
        return issuedBookRepository.getById(issueId);
    }

    public void returnBook(Long issueId, LocalDate realReturnDate) {
        IssuedBook issue = issuedBookRepository.getById(issueId);
        BookEntity book = issue.getBookEntity();
        book.setBookStatus(Status.BOOK_AVAILABLE);
        bookService.updateBook(book);
        History history = new History();
        history.setIssuedBook(issue);
        LocalDate planedEndDate = issue.getIssueEndDate();
        history.setOverdueDays(planedEndDate.until(realReturnDate).getDays());
        issue.setIssueEndDate(realReturnDate);
        issue.setStatus(Status.DELETED);
        issuedBookRepository.save(issue);
        historyRepository.save(history);

    }

}
