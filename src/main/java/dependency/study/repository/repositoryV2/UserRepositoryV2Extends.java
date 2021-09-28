package dependency.study.repository.repositoryV2;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryV2Extends extends UserServiceV2 {

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
