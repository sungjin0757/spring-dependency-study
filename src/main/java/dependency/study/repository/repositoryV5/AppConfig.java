package dependency.study.repository.repositoryV5;

import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
public class AppConfig {

    private final EntityManager em;

    public UserRepositoryV5 userRepository(){
        return new UserRepositoryV5Impl(em);
    }

    public UserServiceV5 userService(){
        return new UserServiceV5(userRepository());
    }
}