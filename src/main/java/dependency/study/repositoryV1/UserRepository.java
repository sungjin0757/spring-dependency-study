package dependency.study.repositoryV1;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public User findOne(Long id){
        return em.find(User.class,id);
    }

    public void remove(Long id){
        User user=findOne(id);
        em.remove(user);
    }
}
