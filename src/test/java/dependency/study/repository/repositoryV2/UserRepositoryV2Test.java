package dependency.study.repository.repositoryV2;

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
public class UserRepositoryV2Test {

    @Autowired
    EntityManager em;

    @Test
    @DisplayName("V2 test")
    void V2_테스트(){

        User user=createUser("hong","1234");

        UserRepositoryV2 userRepository=new UserRepositoryV2Extends(em);

        Long saveId = userRepository.save(user);

        User findUser = userRepository.findById(saveId).get();

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
