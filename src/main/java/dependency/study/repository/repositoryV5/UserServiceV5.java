package dependency.study.repository.repositoryV5;

import dependency.study.domain.User;
import dependency.study.repository.repositoryV4.UserRepositoryV4;
import dependency.study.repository.repositoryV4.UserRepositoryV4Impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Transactional
public class UserServiceV5 {

    private final UserRepositoryV5 userRepositoryV5;

    public UserServiceV5(UserRepositoryV5 userRepositoryV5) {
        this.userRepositoryV5 = userRepositoryV5;
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
