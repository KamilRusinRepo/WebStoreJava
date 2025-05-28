package com.shop.prshop;

import com.shop.prshop.model.user.CustomUserDetails;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomUserDetailsServiceTest {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("securepassword");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        CustomUserDetails result = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

        assertNotNull(result);
        assertEquals(email, result.getUsername());
        assertEquals("securepassword", result.getPassword());

        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        String email = "notfound@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(email)
        );

        assertEquals("Invalid username or password.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
