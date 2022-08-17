package com.selfshelf.project.controller;


import com.selfshelf.project.model.BookEntity;
import com.selfshelf.project.model.Reservation;
import com.selfshelf.project.model.Status;
import com.selfshelf.project.model.UserEntity;
import com.selfshelf.project.service.BookService;
import com.selfshelf.project.service.ReservationService;
import com.selfshelf.project.service.UserServiceImpl;
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
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private final BookService bookService;
    @Autowired
    private final ReservationService reservationService;


    @GetMapping("/{id}")
    public String viewReservation(@PathVariable("id") Long id, Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        BookEntity currentBook = bookService.getBookById(id);
        model.addAttribute("book", currentBook);
        model.addAttribute("user", currentUser);
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = startDate.plusDays(7);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("reservation", new Reservation());

        return "make_reservation";

    }

    @PostMapping("/{id}")
    public String makeReservation(@PathVariable("id") Long id, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        BookEntity currentBook = bookService.getBookById(id);
        currentBook.setBookStatus(Status.BOOK_RESERVED);
        LocalDate startDate = LocalDate.now().minusDays(30);
        LocalDate endDate = startDate.plusDays(7);
        reservationService.reserveBook(currentBook, currentUser, startDate, endDate);
        return "redirect:/books";

    }


    @GetMapping("/active_reservation")
    public String viewReservationsForUserId(Principal principal, Model model) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        Long currentUserId = currentUser.getId();
        List<Reservation> reservations = reservationService.findReservationByUserId(currentUserId);
        model.addAttribute("reservations", reservations);
        model.addAttribute("id", currentUserId);

        return "user_reservations";
    }

    @GetMapping("/active_reservation/all")
    public String viewAllReservations(Principal principal, Model model) {
        List<Reservation> activeReservations = reservationService.findAllActiveReservation();
        model.addAttribute("activeReservations", activeReservations);
        List<Reservation> expiredReservations = reservationService.findAllExpiredReservation(LocalDate.now());
        model.addAttribute("expiredReservations", expiredReservations);
        model.addAttribute("date", LocalDate.now());
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        model.addAttribute("id", currentUser.getId());

        return "admin_reservations";
    }


    @GetMapping("/delete/{id}") //user
    public String deleteById(@PathVariable("id") Long id, Principal principal) {
        reservationService.deleteReservationById(id);

        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.findUserByEmail(currentUserEmail);
        if (currentUser.getUserRole().name().equals("USER")){
            return "redirect:/reservation/active_reservation";
        }else{
            return "redirect:/reservation/active_reservation/all";
        }

    }

}
