package dependency.study.repositoryV2;

import dependency.study.domain.User;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public abstract class UserRepositoryV2 {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public abstract void remove(Long id);
    public abstract Optional<User> findById(Long id);
    public abstract Optional<User> findByName(String name);

}
