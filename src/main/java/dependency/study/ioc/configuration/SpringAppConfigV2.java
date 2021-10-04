//package dependency.study.ioc.configuration;
//
//import dependency.study.repository.repositoryV5.CountingUserRepository;
//import dependency.study.repository.repositoryV5.UserRepositoryV5;
//import dependency.study.repository.repositoryV5.UserRepositoryV5Impl;
//import dependency.study.repository.repositoryV5.UserServiceV5;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
//
//import javax.persistence.EntityManager;
//
//@Configuration
//public class SpringAppConfigV2 {
//
//    @Bean
//    @Primary
//    public LocalEntityManagerFactoryBean getEmf(){
//        LocalEntityManagerFactoryBean emf=new LocalEntityManagerFactoryBean();
//        emf.setPersistenceUnitName("hello");
//        return emf;
//    }
//
//    @Bean
//    @Primary
//    public EntityManager getEm(){
//        return getEmf().getObject().createEntityManager();
//    }
//
//    @Bean
//    public UserRepositoryV5 realUserRepository(){
//        return new UserRepositoryV5Impl(getEm());
//    }
//
//    @Bean
//    public UserRepositoryV5 userRepository(){return new CountingUserRepository(realUserRepository());
//    }
//
//    @Bean
//    public UserServiceV5 userService(){
//        return new UserServiceV5(userRepository());
//    }
//}
