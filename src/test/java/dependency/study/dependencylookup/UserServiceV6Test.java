package dependency.study.dependencylookup;

import dependency.study.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceV6Test {

    UserServiceV6 userService=new UserServiceV6();

    @Test
    @DisplayName("의존관계 검색 테스트")
    void 의존관계_검색(){
        User user=createUser("hong","1234");

        Long saveId = userService.join(user);

        User findUser = userService.findOne(saveId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
