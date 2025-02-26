package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.example.expert.global.common.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    // getUser
    @Test
    void User를_ID로_조회할_수_있다() {
        // given
        String email = "a@a.com";
        long userId = 1L;
        User user = new User(email, "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        UserResponse userResponse = userService.getUser(userId);

        // then
        assertThat(userResponse).isNotNull();
        assertThat(userResponse.getId()).isEqualTo(userId);
        assertThat(userResponse.getEmail()).isEqualTo(email);
    }

    // getUser - UserNotFound
    @Test
    void 존재하지_않는_User를_조회_시_InvalidRequestException을_던진다() {
        // given
        long userId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class, () -> userService.getUser(userId), "User not found");
    }

    // changePassword
    @Test
    void User의_비밀번호를_변경할_수_있다() {
        // given
        String email = "a@a.com";
        long userId = 1L;

        String oldPassword = "password";
        String newPassword = "A1234567";
        UserChangePasswordRequest passwordRequest = new UserChangePasswordRequest(oldPassword, newPassword);

        given(passwordEncoder.encode(oldPassword)).willReturn("encodedOldPassword");
        String encodedOldPassword = passwordEncoder.encode(oldPassword);
        given(passwordEncoder.encode(newPassword)).willReturn("encodedNewPassword");
        String encodedNewPassword = passwordEncoder.encode(newPassword);

        User user = new User(email, encodedOldPassword, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        // 새 비밀번호, 기존 encode 비밀번호 불일치
        given(passwordEncoder.matches(newPassword, user.getPassword())).willReturn(false);
        // 기존 비밀번호, 기존 encode 비밀번호 일치
        given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(true);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userService.changePassword(userId, passwordRequest);

        // then
        assertEquals(encodedNewPassword, user.getPassword());
        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder).matches(oldPassword, encodedOldPassword);
    }

    // changePassword - UserNotFound
    @Test
    void 존재하지_않는_User의_비밀번호_변경_시_InvalidRequestException을_던진다() {
        // given
        long userId = 1L;
        String oldPassword = "password";
        String newPassword = "A1234567";
        UserChangePasswordRequest passwordRequest = new UserChangePasswordRequest(oldPassword, newPassword);

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, passwordRequest), "User not found");
    }

    // changePassword - 새 비밀번호 = 기존 비밀번호
    @Test
    void 동일한_비밀번호로_변경_시_InvalidRequestException을_던진다() {
        // given
        String email = "a@a.com";
        long userId = 1L;

        String oldPassword = "password";
        String newPassword = "password";
        UserChangePasswordRequest passwordRequest = new UserChangePasswordRequest(oldPassword, newPassword);

        given(passwordEncoder.encode(anyString())).willReturn("encodedOldPassword");
        String encodedOldPassword = passwordEncoder.encode(oldPassword);

        User user = new User(email, encodedOldPassword, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        // 새 비밀번호, 기존 encode 비밀번호 일치
        given(passwordEncoder.matches(newPassword, user.getPassword())).willReturn(true);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when & then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, passwordRequest), "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
    }

    // changePassword - 잘못된 비밀번호
    @Test
    void 기존_비밀번호가_일치하지_않는_경우_InvalidRequestException을_던진다() {
        // given
        String email = "a@a.com";
        long userId = 1L;

        String oldPassword = "password";
        String newPassword = "A1234567";
        UserChangePasswordRequest passwordRequest = new UserChangePasswordRequest(oldPassword, newPassword);

        given(passwordEncoder.encode(oldPassword)).willReturn("encodedOldPassword");
        String encodedOldPassword = passwordEncoder.encode(oldPassword);

        User user = new User(email, encodedOldPassword, UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);

        // 새 비밀번호, 기존 encode 비밀번호 불일치
        given(passwordEncoder.matches(newPassword, user.getPassword())).willReturn(false);
        // 기존 비밀번호, 기존 encode 비밀번호 불일치
        given(passwordEncoder.matches(oldPassword, user.getPassword())).willReturn(false);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when & then
        assertThrows(InvalidRequestException.class, () -> userService.changePassword(userId, passwordRequest), "잘못된 비밀번호입니다.");
    }

    // deleteUser
    @Test
    void User를_삭제할_수_있다() {
        // given
        long userId = 1L;
        given(userRepository.existsById(anyLong())).willReturn(true);
        doNothing().when(userRepository).deleteById(anyLong());

        // when
        userService.deleteUser(userId);

        // then
        verify(userRepository, times(1)).deleteById(userId);
    }

    // deleteUser - UserNotFound
    @Test
    void 존재하지_않는_User를_삭제_시_InvalidRequestException를_던진다() {
        // given
        long userId = 1L;
        given(userRepository.existsById(anyLong())).willReturn(false);

        // when & then
        assertThrows(InvalidRequestException.class, () -> userService.deleteUser(userId), "User not found");
        verify(userRepository, never()).deleteById(anyLong());
    }
}
