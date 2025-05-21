package com.shop.prshop.controller;

import com.shop.prshop.RandomString;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;

@Controller
public class SecurityController {

    public UserServiceImpl userService;
    public UserRepository userRepository;
    public PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);


    @Autowired
    public SecurityController(UserServiceImpl userService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/showMyLoginPage")
    public String showMyLoginPage() {
        return "loginpage";
    }

    @GetMapping("/showRegistrationPage")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registerform";
    }

    @PostMapping("/saveUser")
    public String saveUser(User user, Model model, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = user.getEmail();

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            model.addAttribute("errorEmail", true);
            return "registerform";
        }
        else {
            userService.saveUser(user, request.getRequestURL().toString().replace(request.getServletPath(), ""));
        }

        return "registrationSuccess";
    }

    @GetMapping("/showChangePasswordPage")
    public String showChangePasswordPage() {
        return "changePasswordForm";
    }

    @PostMapping("/saveChangedPassword")
    public String saveChangedPassword(Principal principal, Model model,
                                      @RequestParam("oldPassword") String oldPassword,
                                      @RequestParam("newPassword") String newPassword,
                                      @RequestParam("repNewPassword") String repNewPassword) {
        String email = principal.getName();
        Optional<User> optionalUser = userRepository.findByEmail(email);

        User user = optionalUser.get();

        if(!passwordEncoder.matches(oldPassword, user.getPassword())) {
            model.addAttribute("errorOld", true);
        }
        if(!newPassword.equals(repNewPassword)) {
            model.addAttribute("errorNew", true);
        }

        if (passwordEncoder.matches(oldPassword, user.getPassword()) && newPassword.equals(repNewPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            model.addAttribute("succChange", true);
        }

        return "changePasswordForm";
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if(!user.isEnabled()) {
                user.setVerificationCode(null);
                user.setVerified(true);
                userRepository.save(user);
            }
            return "verificationSuccess";
        }
        else {
            logger.info("user with code: " + code + "not found");
            return "verificationFail";
        }

    }

    @GetMapping("/showForgotPasswordForm")
    public String showForgotPasswordForm() {
        return "forgotPasswordForm";
    }

    @PostMapping("/sendResetPasswordLink")
    public String sendResetPasswordLink(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException, UsernameNotFoundException {
        String email = request.getParameter("email");


        try {
            userService.updateResetPassword(email,request.getRequestURL().toString().replace(request.getServletPath(), ""));
            model.addAttribute("success", true);
        }
        catch(UsernameNotFoundException e) {
            model.addAttribute("errorUser", true);
        }
        catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
        return "forgotPasswordForm";
    }

    @GetMapping("/showResetPasswordForm")
    public String showResetPasswordForm(@Param("code") String code, Model model) {
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);
        model.addAttribute("code", code);
        if (optionalUser.isPresent() ){
            return "resetPasswordForm";
        }
        return "invalidToken";
    }

    @PostMapping("saveResetPassword")
    public String saveResetPassword(HttpServletRequest request, Model model) {
        String code = request.getParameter("code");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("passwordRepeat");
        Optional<User> optionalUser = userRepository.findByVerificationCode(code);
        if(optionalUser.isPresent()) {
            if (password.equals(passwordRepeat)) {
                User user = optionalUser.get();
                user.setVerificationCode(null);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                model.addAttribute("success", true);
            }
        }
        else {
            model.addAttribute("error", true);
        }

        return "resetPasswordForm";
    }
}
