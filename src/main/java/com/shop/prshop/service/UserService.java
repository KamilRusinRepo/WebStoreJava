package com.shop.prshop.service;

import com.shop.prshop.model.user.User;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    void saveUser(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;

    List<User> findAllUsers();

    void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    void sendResetPasswordEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
    void updateResetPassword(String email, String siteUrl) throws MessagingException, UnsupportedEncodingException;
}
