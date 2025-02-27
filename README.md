# SPRING ADVANCED

## 🧑‍🏫 프로젝트 소개
[[Spring 5기] Spring 심화 주차 개인 과제](https://teamsparta.notion.site/Spring-5-Spring-1a12dc3ef514802a9785d974abe1ad37)<br><br>
🌱 `Lv1` 코드 개선<br>
🌿 `Lv2` N+1 문제<br>
🪴 `Lv3` 테스트코드 연습<br>
🌳 `Lv4` API 로깅<br>
🌷 `Lv5` 위 제시된 기능 이외 '내'가 정의한 문제와 해결과정<br>
🌹 `Lv6` 테스트 커버리지<br>

## 🗓️ 개발 기간
2025.02.21(금) ~ 2025.02.27(목)
<table>
  <tbody>
    <tr>
      <td align="center">02-21</th>
      <td align="center">02-24</td>
      <td align="center">02-25</td>
      <td align="center">02-26</td>
      <td align="center">02-27</td>
    </tr>
    <tr>
      <td align="center">Lv1<br>Lv2</td>
      <td align="center">Lv3</td>
      <td align="center">Lv4</td>
      <td align="center">Lv5</td>
      <td align="center">Lv6</td>
    </tr>
  </tbody>
</table>

## 🛠️ Project Structure
<details>
  <summary>접기/펼치기</summary> 

  ```
java
 ┗ org
 ┃ ┗ example
 ┃ ┃ ┗ expert
 ┃ ┃ ┃ ┣ domain
 ┃ ┃ ┃ ┃ ┣ auth
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ SigninRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ SignupRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ SigninResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ SignupResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthException.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthService.java
 ┃ ┃ ┃ ┃ ┣ comment
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CommentAdminController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CommentController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ CommentSaveRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CommentResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ CommentSaveResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Comment.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CommentRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ CommentAdminService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ CommentService.java
 ┃ ┃ ┃ ┃ ┣ common
 ┃ ┃ ┃ ┃ ┃ ┣ annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Auth.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthUser.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Timestamped.java
 ┃ ┃ ┃ ┃ ┃ ┗ exception
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ InvalidRequestException.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ServerException.java
 ┃ ┃ ┃ ┃ ┣ manager
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ManagerController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ManagerSaveRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ ManagerResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ManagerSaveResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Manager.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ManagerRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ ManagerService.java
 ┃ ┃ ┃ ┃ ┣ todo
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ TodoController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ TodoSaveRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ TodoResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ TodoSaveResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ Todo.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ TodoRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ TodoService.java
 ┃ ┃ ┃ ┃ ┗ user
 ┃ ┃ ┃ ┃ ┃ ┣ controller
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ UserAdminController.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserController.java
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ UserChangePasswordRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserRoleChangeRequest.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ UserResponse.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserSaveResponse.java
 ┃ ┃ ┃ ┃ ┃ ┣ entity
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ User.java
 ┃ ┃ ┃ ┃ ┃ ┣ enums
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserRole.java
 ┃ ┃ ┃ ┃ ┃ ┣ repository
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserRepository.java
 ┃ ┃ ┃ ┃ ┃ ┗ service
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ UserAdminService.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ UserService.java
 ┃ ┃ ┃ ┣ global
 ┃ ┃ ┃ ┃ ┣ auth
 ┃ ┃ ┃ ┃ ┃ ┣ AuthUserArgumentResolver.java
 ┃ ┃ ┃ ┃ ┃ ┣ JwtFilter.java
 ┃ ┃ ┃ ┃ ┃ ┗ JwtUtil.java
 ┃ ┃ ┃ ┃ ┣ common
 ┃ ┃ ┃ ┃ ┃ ┣ logging
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ annotation
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthLogging.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ request
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomHttpRequestWrapper.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ RequestWrapperFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ response
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┣ CustomHttpResponseWrapper.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┃ ┗ ResponseWrapperFilter.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┣ AuthCheckAspect.java
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ AuthCheckInterceptor.java
 ┃ ┃ ┃ ┃ ┃ ┣ GlobalExceptionHandler.java
 ┃ ┃ ┃ ┃ ┃ ┗ PasswordEncoder.java
 ┃ ┃ ┃ ┃ ┗ config
 ┃ ┃ ┃ ┃ ┃ ┣ FilterConfig.java
 ┃ ┃ ┃ ┃ ┃ ┣ PersistenceConfig.java
 ┃ ┃ ┃ ┃ ┃ ┗ WebConfig.java
 ┃ ┃ ┃ ┣ web
 ┃ ┃ ┃ ┃ ┗ weather
 ┃ ┃ ┃ ┃ ┃ ┣ dto
 ┃ ┃ ┃ ┃ ┃ ┃ ┗ WeatherDto.java
 ┃ ┃ ┃ ┃ ┃ ┗ WeatherClient.java
 ┃ ┃ ┃ ┗ ExpertApplication.java
```

</details>

## 🧪 Test Coverage
<details>
  <summary>[테스트코드 명세] 접기/펼치기</summary> 
  
  ## User

| Repository | findByEmail |  |  | ✅ |
| --- | --- | --- | --- | --- |
| Repository | existByEmail |  |  | ✅ |
| UserService | getUser |  |  | ✅ |
| UserService | getUser | `InvalidRequestException` | `"User not found"` | ✅ |
| UserService | changePassword |  |  | ✅ |
| UserService | changePassword | `InvalidRequestException` | `"User not found"` | ✅ |
| UserService | changePassword | `InvalidRequestException` | `"새 비밀번호는 기존 비밀번호와 같을 수 없습니다."` | ✅ |
| UserService | changePassword | `InvalidRequestException` | `"잘못된 비밀번호입니다."` | ✅ |
| UserService | deleteUser |  |  | ✅ |
| UserService | deleteUser | `InvalidRequestException` | `"User not found"` | ✅ |
| UserAdminService | changeUserRole |  |  | ✅ |
| UserAdminService | changeUserRole | `InvalidRequestException` | `"User not found"` | ✅ |
| UserController | getUser |  |  | ✅ |
| UserController | changePassword |  |  |  |
| UserAdminController | changeUserRole |  |  |  |

## todo

| TodoRepository | findAllByOrderByModifiedAtDesc |  |  | ✅ |
| --- | --- | --- | --- | --- |
| TodoRepository | findByIdWithUser |  |  | ✅ |
| TodoService | saveTodo |  |  | ✅ |
| TodoService | getTodos |  |  | ✅ |
| TodoServicde | getTodo |  |  | ✅ |
| TodoServicde | getTodo | `InvalidRequestException` | `"Todo not found"` | ✅ |
| TodoController | saveTodo |  |  |  |
| TodoController | getTodos |  |  |  |
| TodoController | getTodo |  |  |  |

## manager

| ManagerRepository | findByTodoIdWithUser |  |  | ✅ |
| --- | --- | --- | --- | --- |
| ManagerService | saveManager |  |  | ✅ |
| ManagerService | saveManager | `InvalidRequestException` | `"Todo not found"` |  |
| ManagerService | saveManager | `InvalidRequestException` | `"담당자를 등록하려고 하는 유저가 일정을 만든 유저가 유효하지 않습니다."` | ✅ |
| ManagerService | saveManager | `InvalidRequestException` | `"등록하려고 하는 담당자 유저가 존재하지 않습니다."` |  |
| ManagerService | saveManager | `InvalidRequestException` | `"일정 작성자는 본인을 담당자로 등록할 수 없습니다."` |  |
| ManagerService | getManagers |  |  | ✅ |
| ManagerService | getManagers | `InvalidRequestException` | `"Todo not found"` | ✅ |
| ManagerService | deleteManager |  |  |  |
| ManagerService | deleteManager | `InvalidRequestException` | `"User not found"` |  |
| ManagerService | deleteManager | `InvalidRequestException` | `"Todo not found"` |  |
| ManagerService | deleteManager | `InvalidRequestException` | `"해당 일정을 만든 유저가 유효하지 않습니다."` |  |
| ManagerService | deleteManager | `InvalidRequestException` | `"Manager not found"` |  |
| ManagerService | deleteManager | `InvalidRequestException` | `"해당 일정에 등록된 담당자가 아닙니다."` |  |
  
</details>

![스크린샷 2025-02-27 오전 11 42 08](https://github.com/user-attachments/assets/0fda51a6-820b-4d54-981d-b0c36a26629d)


## ⚙ 개발 환경
- <img src="https://img.shields.io/badge/Java-007396?&style=for-the-badge&logo=java&logoColor=white" /><img src="https://img.shields.io/badge/gradle-%2302303A.svg?&style=for-the-badge&logo=gradle&logoColor=white" /><img src="https://img.shields.io/badge/spring-%236DB33F.svg?&style=for-the-badge&logo=spring&logoColor=white" />
- JDK: `corretto-17 Amazon Corretto 17.0.13 - aarch64`
<!-- <img src="https://img.shields.io/badge/swagger-%2385EA2D.svg?&style=for-the-badge&logo=swagger&logoColor=black" /> -->

## 🔫 트러블 슈팅
[TIL_Spring_심화_트러블슈팅](https://velog.io/@daylikezero/TIL-Spring-%EC%8B%AC%ED%99%94-%ED%8A%B8%EB%9F%AC%EB%B8%94%EC%8A%88%ED%8C%85)<br>
[[과제]_패키지_구조_변경하기](https://velog.io/@daylikezero/%EA%B3%BC%EC%A0%9C-%ED%8C%A8%ED%82%A4%EC%A7%80-%EA%B5%AC%EC%A1%B0-%EB%B3%80%EA%B2%BD%ED%95%98%EA%B8%B0)<br>
[[과제]_에러_메세지_개선](https://velog.io/@daylikezero/%EA%B3%BC%EC%A0%9C-%EC%97%90%EB%9F%AC-%EB%A9%94%EC%84%B8%EC%A7%80-%EA%B0%9C%EC%84%A0)<br>
