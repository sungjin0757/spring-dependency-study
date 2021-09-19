package dependency.study.repositoryV4;

import dependency.study.domain.User;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.Optional;

@RequiredArgsConstructor
public class ActionMakerImpl implements ActionMaker{

    private final EntityManager em;

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
