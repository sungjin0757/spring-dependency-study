package dependency.study.repository.repositoryV1;

import dependency.study.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class UserRepositoryV1Test {

    @Autowired private UserRepositoryV1 userRepository;

    @Test
    @DisplayName("데이터 저장 테스트")
    void 회원저장_Test(){
        User user=User.createUser()
                        .name("홍성진")
                        .password("1234")
                        .build();

        Long saveId = userRepository.save(user);

        User findUser = userRepository.findOne(saveId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }
}
