package org.example.expert.domain.manager.service;

import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.manager.dto.response.ManagerResponse;
import org.example.expert.domain.manager.dto.response.ManagerSaveResponse;
import org.example.expert.domain.manager.entity.Manager;
import org.example.expert.domain.manager.repository.ManagerRepository;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ManagerServiceTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private ManagerService managerService;

    // saveManager
    @Test // 테스트코드 샘플
    void todo가_정상적으로_등록된다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);  // 일정을 만든 유저

        long todoId = 1L;
        Todo todo = new Todo("Test Title", "Test Contents", "Sunny", user);

        long managerUserId = 2L;
        User managerUser = new User("b@b.com", "password", UserRole.USER);  // 매니저로 등록할 유저
        ReflectionTestUtils.setField(managerUser, "id", managerUserId);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId); // request dto 생성

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(userRepository.findById(managerUserId)).willReturn(Optional.of(managerUser));
        given(managerRepository.save(any(Manager.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        ManagerSaveResponse response = managerService.saveManager(authUser, todoId, managerSaveRequest);

        // then
        assertNotNull(response);
        assertEquals(managerUser.getId(), response.getUser().getId());
        assertEquals(managerUser.getEmail(), response.getUser().getEmail());
    }

    // saveManager - TodoNotFound
    @Test
    void manager_등록_시_todo가_존재하지_않는_경우_InvalidRequestException을_던진다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        long todoId = 1L;
        long managerUserId = 2L;

        User managerUser = new User("b@b.com", "password", UserRole.USER);
        ReflectionTestUtils.setField(managerUser, "id", managerUserId);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);

        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class,
                () -> managerService.saveManager(authUser, todoId, managerSaveRequest),
                "Todo not found");

    }

    // saveManager - 담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다.
    @Test
    void todo의_user가_null인_경우_예외가_발생한다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        long todoId = 1L;
        long managerUserId = 2L;

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "user", null);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () ->
                managerService.saveManager(authUser, todoId, managerSaveRequest)
        );

        assertEquals("담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다.", exception.getMessage());
    }

    // saveManager - 등록하려고 하는 담당자 유저가 존재하지 않습니다.
    @Test
    void manager_등록_시_user_가_존재하지_않는_경우_InvalidRequestException을_던진다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        long todoId = 1L;
        long managerUserId = 2L;

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "user", user);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThrows(InvalidRequestException.class, () ->
                managerService.saveManager(authUser, todoId, managerSaveRequest),
                "등록하려고 하는 담당자 유저가 존재하지 않습니다."
        );
    }

    // saveManager - 일정 작성자는 본인을 담당자로 등록할 수 없습니다.
    @Test
    void 일정_작성자와_managerUser_가_일치하는_경우_InvalidRequestException을_던진다() {
        // given
        AuthUser authUser = new AuthUser(1L, "a@a.com", UserRole.USER);
        User user = User.fromAuthUser(authUser);
        long todoId = 1L;
        long managerUserId = 1L;

        Todo todo = new Todo();
        ReflectionTestUtils.setField(todo, "user", user);

        ManagerSaveRequest managerSaveRequest = new ManagerSaveRequest(managerUserId);
        given(todoRepository.findById(anyLong())).willReturn(Optional.of(todo));
        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        // when & then
        assertThrows(InvalidRequestException.class, () ->
                        managerService.saveManager(authUser, todoId, managerSaveRequest),
                "일정 작성자는 본인을 담당자로 등록할 수 없습니다."
        );
    }

    // getManagers
    @Test // 테스트코드 샘플
    public void manager_목록_조회에_성공한다() {
        // given
        long todoId = 1L;
        User user = new User("user1@example.com", "password", UserRole.USER);
        Todo todo = new Todo("Title", "Contents", "Sunny", user);
        ReflectionTestUtils.setField(todo, "id", todoId);

        Manager mockManager = new Manager(todo.getUser(), todo);
        List<Manager> managerList = List.of(mockManager);

        given(todoRepository.findById(todoId)).willReturn(Optional.of(todo));
        given(managerRepository.findByTodoIdWithUser(todoId)).willReturn(managerList);

        // when
        List<ManagerResponse> managerResponses = managerService.getManagers(todoId);

        // then
        assertEquals(1, managerResponses.size());
        assertEquals(mockManager.getId(), managerResponses.get(0).getId());
        assertEquals(mockManager.getUser().getEmail(), managerResponses.get(0).getUser().getEmail());
    }

    // getManagers - TodoNotFound
    // 3-2. 테스트 코드 연습 - 2
    // 1번 케이스
    @Test
    public void manager_목록_조회_시_Todo가_없다면_InvalidRequestException_에러를_던진다() {
        // given
        long todoId = 1L;
        given(todoRepository.findById(todoId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> managerService.getManagers(todoId));
        assertEquals("Todo not found", exception.getMessage());
    }

    // deleteManager
    void manager_삭제에_성공한다() {

    }
    // deleteManager - UserNotFound
    void manager_삭제_시_user가_존재하지_않는_경우_InvalidRequestException을_던진다() {

    }

    // deleteManager - TodoNotFound
    void manager_삭제_시_todo가_존재하지_않는_경우_InvalidRequestException을_던진다() {

    }

    // deleteManager - 해당 일정을 만든 유저가 유효하지 않습니다.
    void manager_삭제_시_작성자_id와_userid가_일치하지_않는_경우_InvalidRequestException을_던진다() {

    }

    // deleteManager - ManagerNotFound
    void manager_삭제_시_manager가_존재하지_않는_경우_InvalidRequestException을_던진다() {

    }

    // deleteManager - 해당 일정에 등록된 담당자가 아닙니다.
    void todo_에_등록된_담당자와_manager_id_가_일치하지_않는_경우_InvalidRequestException을_던진다() {

    }
}
