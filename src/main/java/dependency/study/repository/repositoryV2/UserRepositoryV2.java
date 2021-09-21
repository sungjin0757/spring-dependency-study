package dependency.study.repository.repositoryV2;

import dependency.study.domain.User;

import java.util.Optional;

public abstract class UserRepositoryV2 {

    public abstract Long save(User user);
    public abstract Optional<User> findById(Long userId);
    public abstract void remove(Long userId);
}
