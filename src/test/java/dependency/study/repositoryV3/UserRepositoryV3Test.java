package dependency.study.repositoryV3;


import dependency.study.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class UserRepositoryV3Test {

    @Autowired private UserRepositoryV3 userRepository;

    @Test
    @DisplayName("repositoryV2 테스트")
    void v3_테스트(){
        User user1=createUser("hong","1234");

        Long saveId = userRepository.save(user1);

        User findUser1 = userRepository.findById(saveId).get();
        
        Assertions.assertThat(findUser1.getName()).isEqualTo("hong");

    }

    private User createUser(String name,String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
