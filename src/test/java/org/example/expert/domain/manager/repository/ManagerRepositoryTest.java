package org.example.expert.domain.manager.repository;

import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ManagerRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ManagerRepository managerRepository;

    // findByTodoIdWithUser
    @Test
    void Todo_id로_user를_포함한_manager를_조회할_수_있다() {
        // given
        String email = "a@a.com";
        User user = new User(email, "password", UserRole.USER);
        userRepository.save(user);

        String title = "title";
        String contents = "contents";
        String weather = "weather";
        Todo todo = new Todo(title, contents, weather, user);
        todoRepository.save(todo);

        // when
        List<Manager> foundManagers = managerRepository.findByTodoIdWithUser(todo.getId());

        // then
        assertNotNull(foundManagers);
        assertEquals(foundManagers.get(0).getTodo(), todo);
        assertEquals(foundManagers.get(0).getUser(), user);
    }

}
