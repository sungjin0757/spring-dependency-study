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
public class UserServiceV4Test {

    @Autowired EntityManager em;

    @Test
    @DisplayName("V4 service test")
    void v4_서비스_테스트(){
        User user=createUser("hong","123");

        UserServiceV4 userServiceV4=new UserServiceV4(em);
        Long saveId = userServiceV4.join(user);

        User findUser = userServiceV4.findOne(saveId);

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
