package com.selfshelf.project.controller;

import com.selfshelf.project.model.*;
import com.selfshelf.project.model.projections.BookSearch;
import com.selfshelf.project.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @Autowired
    private IssuedBookService issuedBookService;
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    HistoryService historyService;


    @GetMapping("/books")
    public String listBooks(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("id", currentUser.getId());
        model.addAttribute("books", bookService.getAllAvailableBooks());
        model.addAttribute("allBooks", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/book-main")
    public String showMainBookPage(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "books";
    }

    @GetMapping("/book/new")
    public String createBookForm(Model model) {
        BookEntity book = new BookEntity();
        model.addAttribute("book", book);
        return "create_book";
    }

    @PostMapping("/books")
    public String saveBook(@ModelAttribute("book") BookEntity book) {
        bookService.saveBook(book);

        return "redirect:/books";
    }

    @GetMapping("/books/edit/{id}")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "edit_book";
    }

    @PostMapping("/books/{id}")
    public String updateBook(@PathVariable Long id,
                             @ModelAttribute("book") BookEntity book,
                             Model model) {
        BookEntity existingBook = bookService.getBookById(id);
        existingBook.setId(id);
        existingBook.setAuthor(book.getAuthor());
        existingBook.setTitle(book.getTitle());
        existingBook.setYearOfPublish(book.getYearOfPublish());
        existingBook.setPages(book.getPages());

        //save updated book object
        bookService.updateBook(existingBook);
        return "redirect:/books";
    }

    @GetMapping("/books/{id}")
    public String deleteBook(@PathVariable Long id) {
        BookEntity currentBook = bookService.getBookById(id);
        bookService.deleteBookById(id);
        return "redirect:/books";
    }

    @GetMapping("/books/info/{id}")
    public String getBookInfo(@PathVariable Long id, Model model) {
        BookEntity currentBook = bookService.getBookById(id);
        model.addAttribute("books", currentBook);
        List<Reservation> reservations = reservationService.findActiveReservationsByBookId(id);
        model.addAttribute("reservations", reservations);
        List<IssuedBook> issues = issuedBookService.findIssueBooksByBookIdWithIssueStatusActive(id);
        model.addAttribute("issues", issues);
        List<History> issueHistory = historyService.findIssueHistoryByBookId(id);
        model.addAttribute("history", issueHistory);

        return "books_view_info";
    }


    @GetMapping(value = "/books/search")
    public String search(BookSearch bookSearch, Model model) {
        model.addAttribute("pageName", "Book Search");
        return "search";
    }


    @PostMapping(value = "/books/search")
    public String getSearchedBook(@Valid BookSearch bookSearch, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "result";
        }
        List<BookEntity> books = bookService.search(bookSearch);
        model.addAttribute("books", books);
        return "result";
    }


    @GetMapping(value = "/books/faq")
    public String faq(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("id", currentUser.getId());
        model.addAttribute("pageName", "Book Search");
        return "faq";
    }
}
