package org.example.expert.domain.user.repository;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // findByEmail
    @Test
    void 이메일로_사용자를_조회할_수_있다() {
        // given
        String email = "a@a.com";
        User user = new User(email, "password", UserRole.USER);
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByEmail(email).orElse(null);

        // then
        assertNotNull(foundUser);
        assertEquals(email, foundUser.getEmail());
        assertEquals(UserRole.USER.name(), foundUser.getUserRole().name());
    }

    // existByEmail
    @Test
    void 중복된_이메일인지_확인할_수_있다() {
        // given
        String email = "a@a.com";
        User user = new User(email, "password", UserRole.USER);
        userRepository.save(user);

        // when
        boolean existsByEmail = userRepository.existsByEmail(email);

        // then
        assertTrue(existsByEmail);
    }
}
