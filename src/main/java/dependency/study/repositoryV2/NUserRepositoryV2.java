package dependency.study.repositoryV2;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class NUserRepositoryV2 extends UserRepositoryV2{

    private final EntityManager em;

    @Autowired
    public NUserRepositoryV2(EntityManager em){
        super(em);
        this.em=em;
    }

    @Override
    public void remove(Long id) {
        User findUser = findById(id).orElse(null);
        em.remove(findUser);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class,id));
    }

    @Override
    public Optional<User> findByName(String name) {
        return Optional.ofNullable(em.createQuery("select u from User u where u.name=:name",User.class)
                .setParameter("name",name)
                .getSingleResult());
    }
}
