package dependency.study.repositoryV4;

import dependency.study.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Optional;

@Repository
public class UserRepositoryV4 {

    private final ActionMaker actionMaker;
    private final EntityManager em;

    @Autowired
    public UserRepositoryV4(EntityManager em) {
        this.em=em;
        actionMaker=new ActionMakerImpl(em);
    }
    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public void remove(Long id){
        actionMaker.remove(id);
    }
    public Optional<User> findById(Long id){
        return actionMaker.findById(id);
    }
    public Optional<User> findByName(String name){
        return actionMaker.findByName(name);
    }

}
