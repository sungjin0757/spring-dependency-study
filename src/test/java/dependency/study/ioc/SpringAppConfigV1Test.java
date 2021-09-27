package dependency.study.ioc;

import dependency.study.domain.User;
import dependency.study.ioc.configuration.SpringAppConfigV1;

import dependency.study.repository.repositoryV5.UserServiceV5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SpringAppConfigV1Test {

    @Test
    @DisplayName("Bean Test")
    void 빈_테스트(){
        User user=createUser("hong","123");

        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);

        UserServiceV5 userService= ac.getBean("userService",UserServiceV5.class);

        Long saveId = userService.join(user);

        User findUser = userService.findOne(saveId);

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
