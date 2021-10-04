//package dependency.study.repository.countingRepository;
//
//import dependency.study.domain.User;
//import dependency.study.ioc.configuration.SpringAppConfigV2;
//import dependency.study.repository.repositoryV5.CountingUserRepository;
//import dependency.study.repository.repositoryV5.UserRepositoryV5;
//import dependency.study.repository.repositoryV5.UserServiceV5;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class CountingRepositoryTest {
//
//    AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV2.class);
//
//    @Test
//    @DisplayName("카운팅 리포지토리 테스트")
//    void 카운팅_테스트(){
//        User user1=createUser("hong","1234");
//        User user2=createUser("hong2","1234");
//        User user3=createUser("hong2","1234");
//
//        UserServiceV5 userServiceV5=ac.getBean("userService",UserServiceV5.class);
//
//        userServiceV5.join(user1);
//        userServiceV5.join(user2);
//        userServiceV5.join(user2);
//
//        CountingUserRepository cr=ac.getBean("userRepository", CountingUserRepository.class);
//
//        Assertions.assertThat(cr.getSaveCount()).isEqualTo(3);
//    }
//
//    private User createUser(String name, String password){
//        return User.createUser()
//                .name(name)
//                .password(password)
//                .build();
//    }
//}
