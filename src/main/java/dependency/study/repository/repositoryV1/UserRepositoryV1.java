package dependency.study.repository.repositoryV1;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
public class UserRepositoryV1 {

    private final EntityManager em;

    public Long join(User user){
        return save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public Optional<User> findById(Long id){
        return Optional.ofNullable(em.find(User.class,id));
    }

    public void remove(Long id){
        User user=findOne(id);
        em.remove(user);
    }

}
