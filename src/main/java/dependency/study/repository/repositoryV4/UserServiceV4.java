package dependency.study.repository.repositoryV4;

import dependency.study.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Transactional
public class UserServiceV4 {

    private final UserRepositoryV4 userRepositoryV4;

    public UserServiceV4(EntityManager em) {
        this.userRepositoryV4 = new UserRepositoryV4Impl(em);
    }

    public Long join(User user){
        return userRepositoryV4.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV4.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
