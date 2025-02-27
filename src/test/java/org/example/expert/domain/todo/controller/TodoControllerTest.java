package org.example.expert.domain.todo.controller;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.global.auth.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoService todoService;

    @MockBean
    private JwtUtil jwtUtil; // Interceptor 에서 의존성 주입

    // saveTodo
//    @Test
    void Todo_저장() throws Exception {
        // given
        long userId = 1L;
        String email = "a@a.com";
        UserRole userRole = UserRole.USER;
        UserResponse userResponse = new UserResponse(userId, email);
        AuthUser authUser = new AuthUser(userId, email, userRole);

        long todoId = 1L;
        String title = "title";
        String contents = "contents";
        String weather = "weather";
        TodoSaveRequest todoSaveRequest = new TodoSaveRequest(title, contents);
        given(todoService.saveTodo(authUser, todoSaveRequest))
                .willReturn(new TodoSaveResponse(todoId, title, contents, weather, userResponse));

        // when & then
        mockMvc.perform(post("/todos", authUser, todoSaveRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(todoId))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.contents").value(contents))
                .andExpect(jsonPath("$.weather").value(weather));
    }

    // getTodos
    void Todo_목록_조회() {
        // given

        // when

        // then
    }

    // getTodo
    void Todo_단건_조회() {
        // given

        // when

        // then
    }
}
