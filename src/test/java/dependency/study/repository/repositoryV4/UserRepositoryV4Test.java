package dependency.study.repository.repositoryV4;

import dependency.study.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserRepositoryV4Test {

    @Autowired private UserRepositoryV4 userRepositoryV4;

    @Test
    @DisplayName("V4 테스트")
    void V4_테스트(){
        User user=createUser("hong","123");

        Long saveId = userRepositoryV4.save(user);

        User findUser = userRepositoryV4.findById(saveId).get();

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
