package dependency.study.ioc.configuration;

import dependency.study.repository.repositoryV5.UserRepositoryV5;
import dependency.study.repository.repositoryV5.UserRepositoryV5Impl;
import dependency.study.repository.repositoryV5.UserServiceV5;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.beans.JavaBean;

@Configuration
public class SpringAppConfigV1 {

    @Bean
    @Primary
    public LocalEntityManagerFactoryBean getEmf(){
        LocalEntityManagerFactoryBean emf=new LocalEntityManagerFactoryBean();
        emf.setPersistenceUnitName("hello");
        return emf;
    }

    @Bean
    @Primary
    public EntityManager getEm(){
        return getEmf().getObject().createEntityManager();
    }

    @Bean
    public UserRepositoryV5 userRepository(){
        return new UserRepositoryV5Impl(getEm());
    }

    @Bean
    public UserServiceV5 userService(){
        return new UserServiceV5(userRepository());
    }
}
