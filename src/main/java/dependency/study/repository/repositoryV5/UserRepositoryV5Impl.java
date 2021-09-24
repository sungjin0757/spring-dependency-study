package dependency.study.repository.repositoryV5;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;


@RequiredArgsConstructor
public class UserRepositoryV5Impl implements UserRepositoryV5 {

    private final EntityManager em;

    @Override
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class,userId));
    }

    @Override
    public void remove(Long userId) {
        User findUser = findById(userId).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
        em.remove(findUser);
    }

}
