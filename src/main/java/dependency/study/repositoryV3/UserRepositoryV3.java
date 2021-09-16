package dependency.study.repositoryV3;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryV3 {

    private final SimpleActionMaker simpleActionMaker;
    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public void remove(Long id){
        simpleActionMaker.remove(id);
    }
    public Optional<User> findById(Long id){
        return simpleActionMaker.findById(id);
    }
    public Optional<User> findByName(String name){
        return simpleActionMaker.findByName(name);
    }

}
