package dependency.study.repository.repositoryV3;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Transactional
public class UserServiceV3 {

    private final EntityManager em;
    private final UserRepositoryV3 userRepositoryV3;

    public UserServiceV3(EntityManager em) {
        this.em = em;
        this.userRepositoryV3 = new UserRepositoryV3(em);
    }

    public Long join(User user){
        return userRepositoryV3.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV3.findById(userId);

        return findUser.orElseThrow(()->{
            throw new RuntimeException();
                }
        );
    }
}
