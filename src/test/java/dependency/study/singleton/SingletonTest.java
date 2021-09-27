package dependency.study.singleton;

import dependency.study.ioc.configuration.SpringAppConfigV1;
import dependency.study.repository.repositoryV5.AppConfig;
import dependency.study.repository.repositoryV5.UserServiceV5;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class SingletonTest {

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("자바 코드로 생성한 팩토리 테스트")
    void 자바_오브젝트_테스트(){
        AppConfig appConfig=new AppConfig(em);
        UserServiceV5 userService1=appConfig.userService();
        UserServiceV5 userService2=appConfig.userService();

        Assertions.assertThat(userService1).isNotEqualTo(userService2);
    }

    @Test
    @DisplayName("스프링 빈 애플리케이션 컨텍스트 테스트")
    void 스프링_빈_테스트(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);

        UserServiceV5 userService1=ac.getBean("userService",UserServiceV5.class);
        UserServiceV5 userService2=ac.getBean("userService",UserServiceV5.class);

        Assertions.assertThat(userService1).isEqualTo(userService2);
    }
}
