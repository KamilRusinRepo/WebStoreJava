package com.shop.prshop;

import com.shop.prshop.controller.SecurityController;
import com.shop.prshop.model.user.User;
import com.shop.prshop.repository.UserRepository;
import com.shop.prshop.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SecurityControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Principal principal;

    @InjectMocks
    private SecurityController securityController;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(securityController).build();
    }

    @Test
    public void showMyLoginPage_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showMyLoginPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("loginpage"));
    }

    @Test
    public void showRegistrationPage_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showRegistrationPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("registerform"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void saveUser_emailExists_returnsRegisterFormWithError() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveUser")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("registerform"))
                .andExpect(model().attributeExists("errorEmail"));
    }

    @Test
    public void saveUser_success() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveUser")
                        .flashAttr("user", user))
                .andExpect(status().isOk())
                .andExpect(view().name("registrationSuccess"));

        verify(userService).saveUser(any(User.class), anyString());
    }

    @Test
    public void showChangePasswordPage_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showChangePasswordPage"))
                .andExpect(status().isOk())
                .andExpect(view().name("changePasswordForm"));
    }

    @Test
    public void saveChangedPassword_wrongOldPassword_returnsError() throws Exception {
        User user = new User();
        user.setPassword("encodedPassword");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOld", "encodedPassword")).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveChangedPassword")
                        .principal(principal)
                        .param("oldPassword", "wrongOld")
                        .param("newPassword", "newPass")
                        .param("repNewPassword", "newPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("changePasswordForm"))
                .andExpect(model().attributeExists("errorOld"));
    }

    @Test
    public void saveChangedPassword_passwordsDontMatch_returnsError() throws Exception {
        User user = new User();
        user.setPassword("encodedPassword");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctOld", "encodedPassword")).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveChangedPassword")
                        .principal(principal)
                        .param("oldPassword", "correctOld")
                        .param("newPassword", "newPass")
                        .param("repNewPassword", "differentPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("changePasswordForm"))
                .andExpect(model().attributeExists("errorNew"));
    }

    @Test
    public void saveChangedPassword_success() throws Exception {
        User user = new User();
        user.setPassword("encodedPassword");

        when(principal.getName()).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctOld", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("newEncodedPass");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveChangedPassword")
                        .principal(principal)
                        .param("oldPassword", "correctOld")
                        .param("newPassword", "newPass")
                        .param("repNewPassword", "newPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("changePasswordForm"))
                .andExpect(model().attributeExists("succChange"));

        verify(userRepository).save(any(User.class));
    }

    @Test
    public void verifyUser_validCode_success() throws Exception {
        User user = new User();
        user.setVerified(false);

        when(userRepository.findByVerificationCode("validCode")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/verify")
                        .param("code", "validCode"))
                .andExpect(status().isOk())
                .andExpect(view().name("verificationSuccess"));

        verify(userRepository).save(user);
    }

    @Test
    public void verifyUser_invalidCode_failure() throws Exception {
        when(userRepository.findByVerificationCode("invalidCode")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/verify")
                        .param("code", "invalidCode"))
                .andExpect(status().isOk())
                .andExpect(view().name("verificationFail"));
    }

    @Test
    public void showForgotPasswordForm_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showForgotPasswordForm"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPasswordForm"));
    }

    @Test
    public void sendResetPasswordLink_userNotFound_returnsError() throws Exception {
        doThrow(new UsernameNotFoundException("User not found"))
                .when(userService).updateResetPassword(anyString(), anyString());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sendResetPasswordLink")
                        .param("email", "incorrect"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPasswordForm"))
                .andExpect(model().attributeExists("errorUser"));
    }

    @Test
    public void sendResetPasswordLink_success() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/sendResetPasswordLink")
                        .param("email", "correct"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgotPasswordForm"))
                .andExpect(model().attributeExists("success"));

        verify(userService).updateResetPassword(anyString(), anyString());
    }

    @Test
    public void showResetPasswordForm_validCode_success() throws Exception {
        when(userRepository.findByVerificationCode("validCode")).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showResetPasswordForm")
                        .param("code", "validCode"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPasswordForm"));
    }

    @Test
    public void showResetPasswordForm_invalidCode_returnsInvalidToken() throws Exception {
        when(userRepository.findByVerificationCode("invalidCode")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/showResetPasswordForm")
                        .param("code", "invalidCode"))
                .andExpect(status().isOk())
                .andExpect(view().name("invalidToken"));
    }

    @Test
    public void saveResetPassword_success() throws Exception {
        User user = new User();
        when(userRepository.findByVerificationCode("validCode")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveResetPassword")
                        .param("code", "validCode")
                        .param("password", "newPass")
                        .param("passwordRepeat", "newPass"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPasswordForm"))
                .andExpect(model().attributeExists("success"));

        verify(userRepository).save(user);
    }

    @Test
    public void saveResetPassword_passwordsDontMatch() throws Exception {
        User user = new User();
        when(userRepository.findByVerificationCode("validCode")).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveResetPassword")
                        .param("code", "validCode")
                        .param("password", "newPass")
                        .param("passwordRepeat", "newPas"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPasswordForm"));
    }

    @Test
    public void saveResetPassword_userNotFound() throws Exception {
        when(userRepository.findByVerificationCode("validCode")).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/saveResetPassword")
                        .param("code", "validCode")
                        .param("password", "newPass")
                        .param("passwordRepeat", "newPas"))
                .andExpect(status().isOk())
                .andExpect(view().name("resetPasswordForm"))
                .andExpect(model().attributeExists("error"));

    }
}
