package com.selfshelf.project.controller;

import com.selfshelf.project.model.DAO.UserEntityDao;
import com.selfshelf.project.model.IssuedBook;
import com.selfshelf.project.model.UserEntity;
import com.selfshelf.project.security.CurrentUser;
import com.selfshelf.project.service.BookService;
import com.selfshelf.project.service.IssuedBookService;
import com.selfshelf.project.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller

public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CurrentUser currentUser;
    @Autowired
    private IssuedBookService issuedBookService;

    @GetMapping("/user-reserve")
    public String showUserAccountPage() {
        return "user_reserve";
    }

    //page after login
    @GetMapping("/account")
    public String showAdminAccountPage(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity userLoggedIn = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("fullName", userLoggedIn.getFullName());
        model.addAttribute("currentUser", userLoggedIn);
        model.addAttribute("id", userLoggedIn.getId());
        List<IssuedBook> booksList = issuedBookService.findIssueBooksWithIssueStatusActive();
        model.addAttribute("bookList",booksList);
        return "user_account";
    }

    @GetMapping("/users")
    public String viewPageUsers(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity userLoggedIn = userService.getUserByEmail(currentUserEmail);
        Long currentUserId = userLoggedIn.getId();
        model.addAttribute("id", currentUserId);
        model.addAttribute("listUsers", userService.getUsers());
        model.addAttribute("firstName", userLoggedIn.getFullName());
        model.addAttribute("permissions", userLoggedIn.getUserRole().getGrantedAuthorities());
        return "users";
    }

    @GetMapping("/users/update/{id}")
    public String editUserById(@PathVariable("id") Long id, Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity userLoggedIn = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("pageName", "Edit " + userLoggedIn.getFullName() + " info:");
        UserEntity user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", userLoggedIn);
        return "user_form";
    }

    @PostMapping("/users/update/{id}")
    public String editUser(@PathVariable("id") Long id, @Valid UserEntity user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user_form";
        }
        userService.update(id, user);

        try {
            if (userService.getById(id).getUserRole().name().isEmpty()) {
                return "redirect:/account";
            } else {
                return "redirect:/users";
            }
        } catch (NullPointerException e) {
            return "redirect:/account";
        }

    }

    @PostMapping("/save")
    public String saveUser(UserEntityDao user) {
        userService.saveUser(user);
        return "redirect:/list_users";

    }
}

