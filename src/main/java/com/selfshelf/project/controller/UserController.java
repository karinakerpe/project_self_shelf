package com.selfshelf.project.controller;

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
        System.out.println("------------------principal:--------"+principal.getName());
        UserEntity currentUser = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("fullName", currentUser.getFullName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("id", currentUser.getId());
        List<IssuedBook> booksList = issuedBookService.findIssueBooksWithIssueStatusActive();
        model.addAttribute("bookList",booksList);
        return "user_account";
    }

    @GetMapping("/users")
    public String viewPageUsers(Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.getUserByEmail(currentUserEmail);
        Long currentUserId = currentUser.getId();
        model.addAttribute("id", currentUserId);
        model.addAttribute("listUsers", userService.getUsers());
        model.addAttribute("firstName", currentUser.getFullName());
        model.addAttribute("permissions", currentUser.getUserRole().getGrantedAuthorities());
        return "users";
    }

    @GetMapping("/users/update/{id}")
    public String editUserById(@PathVariable("id") Long id, Model model, Principal principal) {
        String currentUserEmail = principal.getName();
        UserEntity currentUser = userService.getUserByEmail(currentUserEmail);
        model.addAttribute("pageName", "Edit " + currentUser.getFullName() + " info:");
        UserEntity user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("currentUser", currentUser);
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
    public String saveUser(UserEntity user) {
        userService.saveUser(user);
        return "redirect:/list_users";

    }
}

