package dependency.study.repository.repositoryV4;

import dependency.study.domain.User;

import java.util.Optional;

public interface UserRepositoryV4 {

    Long save(User user);
    Optional<User> findById(Long userId);
    void remove(Long userId);
}
