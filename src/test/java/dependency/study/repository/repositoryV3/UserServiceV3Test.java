package dependency.study.repository.repositoryV3;

import dependency.study.domain.User;
import dependency.study.repository.repositoryV4.UserRepositoryV4;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class UserServiceV3Test {

    @Autowired EntityManager em;

    @Test
    @DisplayName("v3 service test")
    void service_v3_테스트(){
        User user=createUser("hong","123");

        UserServiceV3 userServiceV3=new UserServiceV3(em);

        Long joinId = userServiceV3.join(user);

        User findUser = userServiceV3.findOne(joinId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getId()).isEqualTo(joinId);
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
