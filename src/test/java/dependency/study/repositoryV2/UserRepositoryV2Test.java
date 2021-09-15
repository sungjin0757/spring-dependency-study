package dependency.study.repositoryV2;

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
public class UserRepositoryV2Test {

    @Autowired private NUserRepositoryV2 nUserRepositoryV2;
    @Autowired private DUserRepositoryV2 dUserRepositoryV2;

    @Test
    @DisplayName("repositoryV2 테스트")
    void v2_테스트(){
        User user1=createUser("hong","1234");
        User user2=createUser("kim","1234");

        Long saveUserId1 = nUserRepositoryV2.save(user1);
        Long saveUserId2 = dUserRepositoryV2.save(user2);

        User findUser1 = nUserRepositoryV2.findById(saveUserId1).get();
        User findUser2 = dUserRepositoryV2.findById(saveUserId2).get();

        Assertions.assertThat(findUser1.getName()).isEqualTo("hong");
        Assertions.assertThat(findUser2.getName()).isEqualTo("kim");
    }

    private User createUser(String name,String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
