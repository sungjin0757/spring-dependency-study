package dependency.study.dependencylookup;

import dependency.study.domain.User;
import dependency.study.ioc.configuration.SpringAppConfigV1;
import dependency.study.repository.repositoryV5.UserRepositoryV5;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Transactional
public class UserServiceV6 {
    private final UserRepositoryV5 userRepositoryV5;

    public UserServiceV6() {
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);
        this.userRepositoryV5 = ac.getBean("userRepository",UserRepositoryV5.class);
    }

    public Long join(User user){
        return userRepositoryV5.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV5.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
