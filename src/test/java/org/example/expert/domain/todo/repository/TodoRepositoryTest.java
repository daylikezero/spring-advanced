package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;

    // findAllByOrderByModifiedAtDesc
    @Test
    void Todo_목록을_수정일_내림차순으로_조회할_수_있다() {
        // given
        User user = new User("a@a.com", "password", UserRole.USER);
        userRepository.save(user);

        String title = "title";
        String contents = "contents";
        String weather = "weather";
        Todo todo = new Todo(title, contents, weather, user);
        todoRepository.save(todo);

        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(page - 1, size);

        // when
        Page<Todo> todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable);

        // then
        assertNotNull(todos);
    }

    // findByIdWithUser
    @Test
    void Todo_id_로_할일과_작성자를_조회할_수_있다() {
        // given
        String title = "title";
        String contents = "contents";
        String weather = "weather";

        User user = new User("a@a.com", "password", UserRole.USER);
        User saveUser = userRepository.save(user);

        Todo todo = new Todo(title, contents, weather, saveUser);
        todoRepository.save(todo);

        // when
        Todo foundTodo = todoRepository.findById(todo.getId()).orElse(null);

        // then
        assertNotNull(foundTodo);
        assertEquals(title, foundTodo.getTitle());
        assertEquals(contents, foundTodo.getContents());
        assertEquals(weather, foundTodo.getWeather());
        assertEquals(saveUser.getId(), foundTodo.getUser().getId());
    }
}
