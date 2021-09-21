package dependency.study.repository.repositoryV3;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryV3 {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public void remove(Long id){
        User findUser = findById(id).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
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
