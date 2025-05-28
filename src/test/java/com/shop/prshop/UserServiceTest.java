package com.shop.prshop;

import com.shop.prshop.model.user.Role;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.RoleRepository;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.UserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private MimeMessage mimeMessage;

    @Autowired
    private UserServiceImpl userService;

    private User testUser;
    private Role userRole;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("password123");

        userRole = new Role();
        userRole.setName("ROLE_USER");
    }

    @Test
    void saveUser_ShouldSaveUserWithEncryptedPasswordAndSendVerificationEmail() throws MessagingException, UnsupportedEncodingException {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(userRole);
        when(userRepository.count()).thenReturn(1L);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        userService.saveUser(testUser, "http://localhost:8080");

        assertNotNull(testUser.getPassword());
        assertNotEquals("password123", testUser.getPassword());
        assertNotNull(testUser.getVerificationCode());
        assertFalse(testUser.isEnabled());
        assertEquals(Set.of(userRole), testUser.getRoles());

        verify(userRepository, times(1)).save(testUser);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void saveUser_ShouldCreateAdminRoleWhenFirstUser() throws MessagingException, UnsupportedEncodingException {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        when(userRepository.count()).thenReturn(0L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(null);
        when(roleRepository.save(ArgumentMatchers.<Role>any())).thenReturn(adminRole);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        userService.saveUser(testUser, "http://localhost:8080");

        verify(roleRepository, times(1)).save(ArgumentMatchers.<Role>any());
    }

    @Test
    void sendVerificationEmail_ShouldSendEmailWithCorrectContent() throws MessagingException, UnsupportedEncodingException {
        testUser.setVerificationCode("verification-code-123");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        userService.sendVerificationEmail(testUser, "http://localhost:8080");

        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void updateResetPassword_ShouldUpdateCodeAndSendEmailWhenUserExists() throws MessagingException, UnsupportedEncodingException {
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.of(testUser));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        userService.updateResetPassword("john.doe@example.com", "http://localhost:8080");

        assertNotNull(testUser.getVerificationCode());
        verify(userRepository, times(1)).save(testUser);
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void updateResetPassword_ShouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.updateResetPassword("nonexistent@example.com", "http://localhost:8080");
        });
    }

    @Test
    void checkRoleExist_ShouldCreateAdminRoleWhenNoUsers() {
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        when(userRepository.count()).thenReturn(0L);
        when(roleRepository.save(ArgumentMatchers.<Role>any())).thenReturn(adminRole);
    }

    @Test
    void checkRoleExist_ShouldCreateUserRoleWhenUsersExist() {
        when(userRepository.count()).thenReturn(1L);
        when(roleRepository.save(ArgumentMatchers.<Role>any())).thenReturn(userRole);
    }
}
