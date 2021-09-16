package dependency.study.repositoryV3;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class SimpleActionMaker {

    private final EntityManager em;

    public void remove(Long id){
        User findUser = findById(id).orElse(null);
        em.remove(findUser);
    }
    public Optional<User> findById(Long id){
        return Optional.ofNullable(em.find(User.class,id));
    }
    public Optional<User> findByName(String name){
        return Optional.ofNullable(em.createQuery("select u from User u where u.name=:name",User.class)
                .setParameter("name",name)
                .getSingleResult());
    }
}
