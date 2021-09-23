package dependency.study;

import dependency.study.ioc.configuration.AppConfigV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class StudyApplication {

	public static void main(String[] args) {

		SpringApplication.run(StudyApplication.class, args);


	}

	


}
