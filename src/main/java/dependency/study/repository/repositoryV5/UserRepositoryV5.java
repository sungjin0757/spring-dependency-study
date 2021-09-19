package dependency.study.repository.repositoryV5;

import dependency.study.domain.User;

import java.util.Optional;

public interface UserRepositoryV5 {

    Long save(User user);
    Optional<User> findById(Long userId);
    void remove(Long userId);
}
