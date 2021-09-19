package dependency.study.repositoryV4;

import dependency.study.domain.User;

import java.util.Optional;

public interface ActionMaker {
    void remove(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByName(String name);
}
