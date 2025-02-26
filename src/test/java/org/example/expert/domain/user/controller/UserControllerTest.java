package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // getUser
    @Test
    void User_단건_조회() throws Exception {
        // TODO interceptor -> jwtUtil 해결하기
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
    @Test
    void User_비밀번호_변경() throws Exception {
        // given

        // when

        // then
    }
}
