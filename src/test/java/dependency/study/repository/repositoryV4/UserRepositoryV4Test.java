package dependency.study.repository.repositoryV4;

import dependency.study.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class UserRepositoryV4Test {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("V4 테스트")
    void V4_테스트(){
        User user=createUser("hong","123");

        UserRepositoryV4 userRepositoryV4=new UserRepositoryV4Impl(em);

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
