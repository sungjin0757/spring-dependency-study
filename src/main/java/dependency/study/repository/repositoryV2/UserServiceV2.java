package dependency.study.repository.repositoryV2;

import dependency.study.domain.User;

import java.util.Optional;

public abstract class UserServiceV2 {

    public Long join(User user){
        return save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
    public abstract Long save(User user);
    public abstract Optional<User> findById(Long userId);
    public abstract void remove(Long userId);
}
