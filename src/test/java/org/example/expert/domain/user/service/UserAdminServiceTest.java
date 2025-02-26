package org.example.expert.domain.user.service;

import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.user.dto.request.UserRoleChangeRequest;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;

    // changeUserRole
    @Test
    void User의_권한을_변경할_수_있다() {
        // given
        String email = "a@a.com";
        long userId = 1L;
        User user = new User(email, "password", UserRole.USER);
        ReflectionTestUtils.setField(user, "id", userId);
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when
        userAdminService.changeUserRole(userId, new UserRoleChangeRequest("ADMIN"));

        // then
        assertEquals(UserRole.ADMIN, user.getUserRole());
    }


    // changeUserRole - "User not found"
    @Test
    void 존재하지_않는_User의_권한_변경_시_InvalidRequestException을_던진다() {
        // given
        long userId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> userAdminService.changeUserRole(userId, new UserRoleChangeRequest("ADMIN")),
                "User not found");
    }
}
