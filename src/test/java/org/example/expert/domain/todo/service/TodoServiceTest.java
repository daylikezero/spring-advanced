package org.example.expert.domain.todo.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.web.weather.WeatherClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;
    @Mock
    private WeatherClient weatherClient;

    @InjectMocks
    private TodoService todoService;

    // saveTodo
    @Test
    void Todo를_저장할_수_있다() {
        // given
        long userId = 1L;
        String email = "a@a.com";
        UserRole userRole = UserRole.USER;
        User user = new User(email, "password", userRole);

        AuthUser authUser = new AuthUser(userId, email, userRole);

        long todoId = 1L;
        String title = "title";
        String contents = "contents";
        String weather = "weather";
        given(weatherClient.getTodayWeather()).willReturn(weather);
        Todo todo = new Todo(title, contents, weather, user);
        ReflectionTestUtils.setField(todo, "id", todoId);
        given(todoRepository.save(any())).willReturn(todo);

        TodoSaveRequest todoSaveRequest = new TodoSaveRequest(title, contents);

        // when
        TodoSaveResponse todoSaveResponse = todoService.saveTodo(authUser, todoSaveRequest);

        // then
        assertThat(todoSaveResponse).isNotNull();
        assertThat(todoSaveResponse.getId()).isEqualTo(todoId);
    }

    // getTodos
    @Test
    void Todo_목록을_조회할_수_있다() {
        // given
        String email = "a@a.com";
        UserRole userRole = UserRole.USER;
        User user = new User(email, "password", userRole);

        String title = "title";
        String contents = "contents";
        String weather = "weather";
        Todo todo = new Todo(title, contents, weather, user);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Todo> todos = new PageImpl<>(List.of(todo), pageable, 0L);
        given(todoRepository.findAllByOrderByModifiedAtDesc(any())).willReturn(todos);

        // when
        Page<TodoResponse> todoResponses = todoService.getTodos(page, size);

        // then
        assertThat(todoResponses).isNotNull();
        assertThat(todoResponses.getTotalElements()).isEqualTo(1);
    }

    // getTodo
    @Test
    void 단건_todo를_조회할_수_있다() {
        // given
        String email = "a@a.com";
        UserRole userRole = UserRole.USER;
        User user = new User(email, "password", userRole);

        Long todoId = 1L;
        String title = "title";
        String contents = "contents";
        String weather = "weather";
        Todo todo = new Todo(title, contents, weather, user);
        ReflectionTestUtils.setField(todo, "id", todoId);
        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.of(todo));

        // when
        TodoResponse todoResponse = todoService.getTodo(todoId);

        // then
        assertThat(todoResponse).isNotNull();
        assertThat(todoResponse.getId()).isEqualTo(todoId);
    }

    // getTodo - TodoNotfound
    @Test
    void 존재하지_않는_todo를_조회_시_InvalidRequestException을_던진다() {
        // given
        long todoId = 1L;
        given(todoRepository.findByIdWithUser(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> todoService.getTodo(todoId),
                "Todo not found");
    }
}
