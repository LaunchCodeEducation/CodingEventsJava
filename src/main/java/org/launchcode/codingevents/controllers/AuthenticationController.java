package org.launchcode.codingevents.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.launchcode.codingevents.dto.LoginFormDTO;
import org.launchcode.codingevents.dto.RegisterFormDTO;
import org.launchcode.codingevents.exception.UserRegistrationException;
import org.launchcode.codingevents.models.User;
import org.launchcode.codingevents.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private SecurityContextRepository securityContextRepository;

    private static final String userSessionKey = "user";

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register for Coding Events");
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, Model model) {
        model.addAttribute("title", "Register for Coding Events");

        if (errors.hasErrors()) {
            return "register";
        }

        User existingUser = userService.findByUsername(registerFormDTO.getUsername());

        if (existingUser != null) {
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            return "register";
        }

        try {
            User newUser = userService.save(registerFormDTO);
        } catch (UserRegistrationException ex) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            return "register";
        }

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In to Coding Events");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors,
                                   HttpServletRequest request,
                                   HttpServletResponse response,
                                   Model model) {
        model.addAttribute("title", "Log In to Coding Events");
        if (errors.hasErrors()) {
            return "login";
        }

        try {
            UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken.unauthenticated(
                    loginFormDTO.getUsername(),
                    loginFormDTO.getPassword()
                );
            Authentication authentication =
                authManager.authenticate(token);


            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            this.securityContextRepository.saveContext(context, request, response);

            return "redirect:";
        } catch (AuthenticationException ex) {
            errors.rejectValue("username", "bad.credentials", "Invalid e-mail or password");
            return "/login";
        }
    }

}
