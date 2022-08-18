package com.selfshelf.project.controller;


import com.selfshelf.project.model.DAO.UserEntityDao;
import com.selfshelf.project.model.UserEntity;
import com.selfshelf.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class SecurityController {
    @Autowired
    private UserService service;

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new UserEntity());

        return "signup_form";
    }

    @GetMapping("/register_admin")
    public String showSignUpFormForAdmin(Model model) {
        model.addAttribute("user", new UserEntity());

        return "signup_form_admin";
    }

    @PostMapping("/process_register")
    public String processRegistration(UserEntityDao user) {
        service.saveUser(user);

        return "registration_successful";

    }

    @PostMapping("/process_register_admin")
    public String processRegistrationAdminAddUser (UserEntityDao user) {
        service.saveUser(user);
        return "registration_successful";

    }

    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
}
