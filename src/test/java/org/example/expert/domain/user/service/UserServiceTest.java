package org.example.expert.domain.user.service;

import net.datafaker.Faker;
import org.example.expert.domain.user.dto.response.UserSearchResponse;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private final Faker faker = new Faker();
    private List<User> userList;
    private String existingNickName; // 실제 존재하는 닉네임
    class MockUserSearchResponse implements UserSearchResponse {
        private final Long id;
        private final String email;
        private final String nickName;

        public MockUserSearchResponse(Long id, String email, String nickName) {
            this.id = id;
            this.email = email;
            this.nickName = nickName;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getNickName() {
            return nickName;
        }
    }
    @BeforeEach
    void setUp() {
        // 100만 개의 더미 데이터 생성
        userList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            // 랜덤 이메일
            String email = faker.internet()
                    .emailAddress();
//            // 랜덤 비밀번호
//            String password = faker.internet()
//                    .password(8, 16, true, true, true);
            // 랜덤 닉네임
            String nickname = faker.funnyName().name();
            User user = new User(email,"1234", UserRole.ROLE_USER, nickname);
            userList.add(user);
        }
        existingNickName = userList.get(faker.random().nextInt(0, userList.size() - 1)).getNickName();
    }
    @Test
    void getUsers_Success() {
        // given
        List<UserSearchResponse> expectedUsers = userList.stream()
                .filter(user -> user.getNickName().equals(existingNickName))
                .map(user -> new MockUserSearchResponse(user.getId(), user.getEmail(), user.getNickName()))
                .collect(Collectors.toList());
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();


        when(userRepository.findByNickName(existingNickName)).thenReturn(expectedUsers);

        // when
        List<UserSearchResponse> result = userService.getUsers(existingNickName);


        stopWatch.stop();
        // then
        assertNotNull(result);
        System.out.println(stopWatch.prettyPrint());
    }
}