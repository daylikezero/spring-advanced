package org.example.expert.domain.user.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.service.UserService;
import org.example.expert.global.auth.AuthUserArgumentResolver;
import org.example.expert.global.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil; // Interceptor 에서 의존성 주입

    @MockBean
    private AuthUserArgumentResolver authUserArgumentResolver;

    // getUser
    @Test
    void User_단건_조회() throws Exception {
        // given
        long userId = 1L;
        String email = "a@a.com";
        given(userService.getUser(userId)).willReturn(new UserResponse(userId, email));

        // when & then
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.email").value(email));
    }


    // changePassword
//    @Test
    void User_비밀번호_변경() throws Exception {
        // given
        long userId = 1L;
        String email = "a@a.com";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        AuthUser authUser = new AuthUser(1L, email, UserRole.USER);

        UserChangePasswordRequest userChangePasswordRequest = new UserChangePasswordRequest(oldPassword, newPassword);
        willDoNothing().given(userService).changePassword(userId, userChangePasswordRequest);

        // TODO jwt token mocking

        // when
        mockMvc.perform(put("/users", authUser, userChangePasswordRequest)
                        .header("Authorization", "Bearer mock-token"))
                .andExpect(status().isOk());
        // then
        verify(userService, times(1)).changePassword(userId, userChangePasswordRequest);
    }
}
