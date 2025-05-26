package com.shop.prshop.service;

import com.shop.prshop.RandomString;
import com.shop.prshop.controller.SecurityController;
import com.shop.prshop.model.user.Role;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.RoleRepository;
import com.shop.prshop.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void saveUser(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        Role role;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String verificationCode = RandomString.generate(64);
        user.setVerificationCode(verificationCode);
        user.setVerified(false);

        if(userRepository.count() == 0) {
            role = roleRepository.findByName("ROLE_USER");
        }
        else {
            role = roleRepository.findByName("ROLE_USER");
        }


        if(role == null) {
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String emailAddress = user.getEmail();
        String fromAddress = "sys.for.verif@gamil.com";
        String senderName = "Premium Reseler";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>" +
                "Click the link below to verify your registration:<br>" +
                "<h3><a href=[[url]]>VERIFY</a></h3>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(emailAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteUrl + "/verify?code=" + user.getVerificationCode();
        content = content.replace("[[url]]", verifyURL);
        logger.info(siteUrl);
        logger.info(verifyURL);
        logger.info(content);
        helper.setText(content, true);
        javaMailSender.send(message);
    }

    @Override
    public void sendResetPasswordEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        String fromAddress = "sys.for.verif@gamil.com";
        String senderName = "Premium Reseler";
        String subject = "Link to reset your password";
        String content = "Dear [[name]],<br>" +
                "Click the link below to reset your password:<br>" +
                "<h3><a href=[[url]]>RESET PASSWORD</a></h3>" +
                "<br>" +
                "<p>Ignore this email if you do remember your password, " +
                "or you have not made the request.</p>";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName());
        String verifyURL = siteUrl + "/showResetPasswordForm?code=" + user.getVerificationCode();
        content = content.replace("[[url]]", verifyURL);
        logger.info(siteUrl);
        logger.info(verifyURL);
        logger.info(content);
        helper.setText(content, true);
        javaMailSender.send(message);
    }


    @Override
    public void updateResetPassword(String email, String siteUrl) throws MessagingException, UnsupportedEncodingException, UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        String resetPasswordCode = RandomString.generate(64);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setVerificationCode(resetPasswordCode);
            userRepository.save(user);
            sendResetPasswordEmail(user, siteUrl);
        }
        else {
            throw new UsernameNotFoundException("User with email: " + email + "not found");
        }
    }



    private Role checkRoleExist() {
        Role role = new Role();

        if(userRepository.count() == 0) {
            role.setName("ROLE_ADMIN");
        }
        else {
            role.setName("ROLE_USER");
        }
        return roleRepository.save(role);
    }
}
