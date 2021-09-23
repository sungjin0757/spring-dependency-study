package dependency.study.repository.repositoryV5;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class V5Test {

    @PersistenceContext EntityManager em;

    @Test
    @DisplayName("v5 repositoryTest")
    void v5_repository_테스트(){
        User user=createUser("hong","1234");

        UserRepositoryV5 userRepository=new UserRepositoryV5Impl(em);

        Long saveId= userRepository.save(user);

        User findUser=userRepository.findById(saveId).get();

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    @DisplayName("v5 total test")
    void v5_통합_테스트(){
        User user=createUser("hong","123");

        AppConfig appConfig=new AppConfig(em);
        UserServiceV5 userService= appConfig.userService();

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
