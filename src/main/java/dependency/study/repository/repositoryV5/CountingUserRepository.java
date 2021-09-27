package dependency.study.repository.repositoryV5;

import dependency.study.domain.User;
import lombok.Getter;

import java.util.Optional;

@Getter
public class CountingUserRepository implements UserRepositoryV5{

    int saveCount=0;
    int removeCount=0;
    int findCount=0;

    private final UserRepositoryV5 userRepositoryV5;

    public CountingUserRepository(UserRepositoryV5 userRepositoryV5){
        this.userRepositoryV5=userRepositoryV5;
    }

    @Override
    public Long save(User user) {
        saveCount++;
        return userRepositoryV5.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        findCount++;
        return userRepositoryV5.findById(userId);
    }

    @Override
    public void remove(Long userId) {
        removeCount++;
        userRepositoryV5.remove(userId);
    }

}
