package com.selfshelf.project.controller;


import com.selfshelf.project.model.*;
import com.selfshelf.project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/issued")
public class IssuedBookController {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final BookService bookService;
    @Autowired
    private final ReservationService reservationService;
    @Autowired
    private final IssuedBookService issuedBookService;
    @Autowired
    HistoryService historyService;


    @GetMapping("/{id}") // Admin
    public String issueBook(@PathVariable("id") Long id, Model model, Principal principal) {

        BookEntity currentBook = bookService.getBookById(id);
        Reservation currentReservation = new Reservation();

        List<Reservation> reservationIdList = currentBook.getReservations();
        for (Reservation reservation : reservationIdList
        ) {
            currentReservation = reservation;
        }
        reservationIdList.clear();

        issuedBookService.issueBookWithActiveReservation(currentBook, currentReservation.getUserEntity());
        reservationService.deleteByIdUpdatingBookStatus(currentReservation.getId());
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        if (currentUser.getUserRole().name().equals("USER")){
        return "redirect:/books";
        }else{
            return "redirect:/issued/admin";
        }
    }

    @GetMapping("/admin")
    public String viewIssuedBooksForAdminId(Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        model.addAttribute("currentUserId", currentUser.getId());

        List<IssuedBook> issuedBooks = issuedBookService.findIssueBooksWithIssueStatusActive();
        model.addAttribute("issuedBooks", issuedBooks);
        issuedBookService.showAllIssuedBooks();
        LocalDate localDate = LocalDate.now();
        model.addAttribute("date", localDate);
        return "issued_admin";
    }

    @GetMapping("/user")
    public String viewIssuedBooksForUserId(Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        Long currentUserId = currentUser.getId();
        List<IssuedBook> issuedBooksActive = issuedBookService.findIssuedBooksByUserIdActive(currentUserId);
        model.addAttribute("issuedBooksActive", issuedBooksActive);
        List<History> issuedBooksHistory = historyService.findIssueHistoryByUserId(currentUserId);
        model.addAttribute("issuedBooksHistory", issuedBooksHistory);
        model.addAttribute("currentUserId", currentUserId);
        LocalDate localDate = LocalDate.now();
        model.addAttribute("date", localDate);
        return "issued_user";
    }

    @GetMapping("return/{id}") // Admin
    public String viewReturnDetails(@PathVariable("id") Long id, Model model, Principal principal) {
        IssuedBook currentIssuedBook = issuedBookService.getIssuedBookById(id);
        model.addAttribute("currentIssueBook", currentIssuedBook);
        LocalDate planedReturnDate = currentIssuedBook.getIssueEndDate();
        model.addAttribute("planedReturnDate", planedReturnDate);
        LocalDate realReturnDate = LocalDate.now();
        model.addAttribute("realReturnDate", realReturnDate);
        int daysUntil = planedReturnDate.until(realReturnDate).getDays();
        model.addAttribute("daysOverdue", daysUntil);
        return "return_book";

    }

    @PostMapping("return/{id}") // Admin
    public String returnIssuedBookByIssueId(@PathVariable("id") Long id, Model model, Principal principal) {
        LocalDate realReturnDate = LocalDate.now();
        issuedBookService.returnBook(id, realReturnDate);
        return "redirect:/issued/admin";

    }

}
