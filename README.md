## 오브젝트와 의존관계 STUDY
***

## 🎯 목차
#### 📌 스프링의 지향점
#### 📌 관심사의 분리
#### 📌 제어의 역전 (IOC)
#### 📌 스프링 IOC
#### 📌 싱글톤 레지스트리
#### 📌 의존관계 주입(DI)
***
## 🚀 스프링의 지향점
**스프링은 자바를 기반으로 한 기술!**
- 자바에서 가장 중요하게 가치를 두는 것은 객체지향
  - So, 스프링이 가장 관심을 많이 두는 대상은 오브젝트 입니다.
  스프링을 이해하려면 먼저 오브젝트에 깊은 관심을 가져야 하며, 애플리케이션에서 오브젝트가 생성되고
  다른 오브젝트와 관계를 맺고, 사용되고, 소멸하기까지의 전 과정을 이해하고 있어야합니다.
  
  - 오브젝트에 대한 관심은 오브젝트의 설계에 대한 관심으로 발전
    - 다양한 목적을 위해 재활용 가능한 설계 방법인 디자인 패턴
    - 좀 더 깔끔한 구조가 되도록 지속적으로 개선해나가는 작업인 리팩토링
    - 테스트
    
- 객체지향 기술과 설계, 구현에 관한 실용적인 전략을 개발자들이 자연스럽고 손쉽게 적용할 수 있도록 프레임워크 형태로 제공
  

***
## 🚀 관심사의 분리

### 🔍 테스트 내용

**아래의 3가지 `repository`, `service`, `domain` 을 활용하여 아무 설정도 안되어 있는 난감한
상태에서 제가 학습한 나름대로 Ver 1. 부터 Ver 5. 까지의 스프링이 지향하는 상태로의 발전을 보여드리겠습니다.**
- 사용자 정보를 데이터베이스에 저장할 `repository`
  - 편의를 위해 `JPA`를 사용하겠습니다.
- 도메인 정보를 활용하여 핵심 비즈니스 로직을 담당할 `service`
- 유저 정보를 담고 있는 `domain`

**User.java (모든 Version 에서 사용할 도메인)**
```java
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name="user_id")
    private Long id;

    private String name;
    private String password;

    @Builder(builderMethodName = "createUser")
    public User(String name,String password){
        this.name=name;
        this.password=password;
    }

}
```

### 🔧 Version 1. 
- `repository` 클래스만 존재

**UserRepositoryV1.java**

```java
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
public class UserRepositoryV1 {

  private final EntityManager em;

  public Long join(User user) {
    return save(user);
  }

  public User findOne(Long userId) {
    Optional<User> findUser = findById(userId);

    return findUser.orElseThrow(() -> {
              throw new RuntimeException();
            }
    );
  }

  public Long save(User user) {
    em.persist(user);
    return user.getId();
  }

  public Optional<User> findById(Long id) {
    return Optional.ofNullable(em.find(User.class, id));
  }

  public void remove(Long id) {
    User user = findOne(id);
    em.remove(user);
  }

}


```

**UserRepositoryV1Test.java(동작 확인)**
```java
@Transactional
@SpringBootTest
public class UserRepositoryV1Test {

  @Autowired
  EntityManager em;

  @Test
  @DisplayName("데이터 저장 테스트")
  void 회원저장_Test(){
    User user=User.createUser()
            .name("홍성진")
            .password("1234")
            .build();

    UserRepositoryV1 userRepository=new UserRepositoryV1(em);
    Long saveId = userRepository.save(user);

    User findUser = userRepository.findOne(saveId);

    Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
    Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
  }
}
```

**테스트 결과**

<img width="50%" alt="스크린샷 2021-09-27 오후 9 04 08" src="https://user-images.githubusercontent.com/56334761/134904927-cd84d2d8-0e8a-40c1-9a1f-4c0c8b3b089f.png">

**이런 식으로 `repository` 클래스만 딱 쓰는 것은 객체지향관점에서 굉장한 문제를 초래한다고 합니다.**
- 정상적으로 동작하는 것이 확인 되었습니다. 근데 문제가??!
- 문제를 개선했을 때의 장점은?
- 미래에 주는 유익?!

**이러한 고찰(좋은 객체지향 설계)이 스프링을 활용한 개발의 첫 걸음이 되었다고 합니다.**

- **관심사의 분리**
  - 객체지향의 세계는 모든 것(오브젝트에 대한 설계와 이를 구현한 코드)이 변함.
    - 사용자의 요구사항에 따라
    - 시간이 변함에 따라
    - ...
  - **So,** 개발자가 객체를 설계할 때 가장 염두해야할 사항은 바로 미래의 변화를 어떻게 대처할 것인가? 입니다.
    - **ex)** 기능 변화에 대한 요구사항을 미래를 잘 대비한 개발자는 단지 몇 줄의 코드 수정 및 검증 까지의 시간이 5분이었다면,
    미래를 대비하지 않은 개발자는 5시간이 걸릴 수 있다는 것입니다.
  - 객체 지향 기술은 추상세계를 자유롭게 변경, 확장시킬 수 있다는 것에 의미가 있습니다.
  - 분리와 확장을 고려한 설계를 하면 필요한 작업을 최소화하고, 변경이 다른 작업에 문제를 일으키지 않을 것입니다.

- **분리**
  - 모든 변경과 발전은 한 번에 한 가지 관심사항에 집중해서 일어납니다.
    - **ex)** DB의 접속용 암호를 변경하라
    - 여기서 일어나는 문제는 변화에 따른 작업은 한 곳에 집중되지 않다는 경우가 많드는 것입니다. 즉, DB 접속 암호를 변경하기위해 수 백개의 클래스를
    돌아다니며, 확인을 해야할 수도 있다는 것입니다.
  - **So, 문제를 해결하기 위해?** 공통 관심사항을 가진 것들을 한 곳에 집중시키면 됩니다.
  - 관심이 같은 것끼리는 하나의 객체 안으로 , 다른 것은 따로 떨어져 서로 영향을 끼치지 않도록 하는 것입니다.
  
  - **분리의 예**
    - 중복 코드의 메소드 추출 (리팩토링의 메소드 추출 기법)
      - 이점 : 만일 중복 코드가 수 천개의 메소드에 흩어져 있다고 생각합시다. 근데, 중복 코드에 대한 변경이 필요하다면?? 수 천개의 메소드를
      일일히 수정하고 있어야할 것입니다. 만일 중복 코드를 메소드로 추출하여놨으면, 딱 한 곳만 바꾸기만 하면 되죠..
    

**만약, `repository`의 로직을 상황에 따라 바꾸고 싶다고 가정해 봅시다.**

하지만, 현재, Ver 1. 에서는 `repository`클래스 하나로만 구성되어 있습니다. 즉, 사용 시점마다 원하는 로직에 대한 코드로의 번거로운 수정이
필요하게 됩니다. 또한, 이 클래스를 개발한 개발자가 그만두고 다른 개발자가 이어서 작업을 한다고 생각한다면, 어느 부분을 수정해야 할 지도
모를 수 있으며 관심사가 다른 메소드들이 한 클래스내에 다 존재하고 있습니다.

**이러한 문제를 해결을 하기 위해서 기존 코드를 분리할 시점에 도달했습니다.**

### 🔧 Version 2.
Version 1. 에서의 코드를 보시면 한 가지 클래스내에 데이터베이스에 접근하는 메소드와 데이터베이스를 이용하여 비즈니스 로직을 담당하고 있는 메소드가 있습니다.
이 둘의 관심사를 `repository` 와 `service`로 분리시켜 봅시다.
어떻게 할까요??!
- 상속을 통한 확장
 
**UserRepositoryV2.java**

```java
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

```

**UserRepositoryV2Extends.java**

```java
@RequiredArgsConstructor
public class UserRepositoryV2Extends extends UserServiceV2{

    private final EntityManager em;

    @Override
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class,userId));
    }

    @Override
    public void remove(Long userId) {
        User findUser = findById(userId).orElse(null);

        if(findUser==null)
            throw  new IllegalArgumentException();

        em.remove(findUser);
    }
}
```

**UserServiceV2Test.java**

```java
@SpringBootTest
@Transactional
public class UserServiceV2Test {

  @Autowired
  EntityManager em;

  @Test
  @DisplayName("V2 test")
  void V2_테스트(){

    User user=createUser("hong","1234");

    UserServiceV2 userService=new UserRepositoryV2Extends(em);

    Long saveId = userService.join(user);

    User findUser = userService.findOne(saveId).get();

    Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
    Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
  }

  private User createUser(String name, String password){
    return User.createUser()
            .name(name)
            .password(password)
            .build();
  }
}
```

**테스트 결과**

<img width="50%" alt="스크린샷 2021-09-28 오후 5 33 37" src="https://user-images.githubusercontent.com/56334761/135062980-7c4e4236-43ad-48e1-8ff4-e6e0d792cba9.png">

**추상 클래스를 활용하여 `repository`의 변경 작업을 한층 더 용이하게 사용할 수 있게 되었습니다.**
- 단순히 변경 이상으로 손쉽게 확장할 수 도 있게 되었습니다.
- 이제는 `repository`클래스의 코드를 변경할 필요없이 기능을 새롭게 정의한 클래스를 만들 수 있습니다.

**👍 템플릿 메소드 패턴**
>기본적인 로직의 흐름을 만들고, 그 기능의 일부를 추상 메소드나 오버라이딩이 가능한 메소드 등으로 만든 뒤
서브클래스에서 이런 메소드를 필요에 맞게 구현해서 사용하도록 하는 방법을 디자인 패턴에서 템플릿 메소드 패턴이라고 합니다.

**BUT, 변경 및 확장이 용이해졌지만 상속을 활용했다는 단점이 존재**
- 자바는 다중상속 허용 금지.
- 서브 클래스가 슈퍼 클래스의 기능을 사용할 수 있음.
  - 슈퍼 클래스 내부의 변경이 있을 때, 모든 서브클래스의 내용을 수정해야할 수도 있음. 즉, 관심사의 분리가 완벽하지 않다는 뜻.

### 🔧 Version 3.
- 독립적인 클래스로 분리하여 관심사를 분리
  - `repository` 관련된 부분을 상속클래스가 아닌 아예 별도의 클래스로 만들어봅시다.

**UserRepositoryV3.java**
```java
@RequiredArgsConstructor
public class UserRepositoryV3 {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public void remove(Long id){
        User findUser = findById(id).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
        em.remove(findUser);
    }
    public Optional<User> findById(Long id){
        return Optional.ofNullable(em.find(User.class,id));
    }
    public Optional<User> findByName(String name){
        return Optional.ofNullable(em.createQuery("select u from User u where u.name=:name",User.class)
                .setParameter("name",name)
                .getSingleResult());
    }

}
```

**UserServiceV3.java**
```java
@Transactional
public class UserServiceV3 {

  private final EntityManager em;
  private final UserRepositoryV3 userRepositoryV3;

  public UserServiceV3(EntityManager em) {
    this.em = em;
    this.userRepositoryV3 = new UserRepositoryV3(em);
  }

  public Long join(User user){
    return userRepositoryV3.save(user);
  }

  public User findOne(Long userId){
    Optional<User> findUser=userRepositoryV3.findById(userId);

    return findUser.orElseThrow(()->{
              throw new RuntimeException();
            }
    );
  }
}
```

**UserServiceV3Test.java**
```java
@SpringBootTest
@Transactional
public class UserServiceV3Test {

    @Autowired EntityManager em;

    @Test
    @DisplayName("v3 service test")
    void service_v3_테스트(){
        User user=createUser("hong","123");

        UserServiceV3 userServiceV3=new UserServiceV3(em);

        Long joinId = userServiceV3.join(user);

        User findUser = userServiceV3.findOne(joinId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getId()).isEqualTo(joinId);
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
```

**동작 확인**

<img width="50%" alt="스크린샷 2021-09-28 오후 7 10 32" src="https://user-images.githubusercontent.com/56334761/135067909-22b78bb8-3a91-4eec-85b4-ef97401543e6.png">

**동작이 되는 것을 확인하실 수 있습니다.**

하지만, 한 눈에 보더라도 많은 문제가 존재하죠!
- `repository`의 확장이 다시 불가능해졌습니다.
  - `service`의 코드가 특정 `repository`에 종속되었기 때문입니다. 
  - 만일, `repsitory`의 특정 메소드 이름이 바뀌고, 이 메소드를 사용하는 곳이 수 백개 이상이라면 일일히 다 찾아가서 수정하는 작업은 너무 번거롭게 됩니다.
- `repsotory`가 어떤 클래스인지 `service`에서 구체적으로 알고 있어야 합니다. 즉, 이 또한 클래스의 변경이 일어난다면, 사용하고 있는 클래스에서 일일히 수정을 해야합니다.

**결론적으로, 이런 식으로 설계가 된다면, 어떤 클래스가 쓰일지 어떤 메소드가 쓰일지에 대한 정보를 일일히 다 알고 있어야 합니다.**
- 따라서 `service`는 구체적인 `repository`클래스에 종속될수 밖에 없습니다.

**상속의 문제를 해결하고자 한 것이 더 많은 문제를 초래하게 되었습니다.**

### 🔧 Version 4.
- 인터페이스의 도입
  - 두 개의 클래스가 서로 긴밀하게 연결되어 있지 않도록 
  - 자바가 추상화를 위해 제공하는 가장 유용한 도구
  - 오브젝트를 만드려면 구체적인 클래스 하나를 선택해야겠지만 접근하는 쪽에서는 오브젝트를 만들 때 사용할 클래스가 무엇인지 몰라도 됨.
  - 인터페이스는 어떤 일을 하겠다는 기능만 정의해놓은 것입니다.

**UserRepositoryV4.java**
```java
public interface UserRepositoryV4 {

    Long save(User user);
    Optional<User> findById(Long userId);
    void remove(Long userId);
}

```

**UserRepositoryV4Impl.java**
```java
@RequiredArgsConstructor
public class UserRepositoryV4Impl implements UserRepositoryV4{

    private final EntityManager em;

    @Override
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class,userId));
    }

    @Override
    public void remove(Long userId) {
        User findUser = findById(userId).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
        em.remove(findUser);
    }
}

```

**UserServiceV4.java**
```java
@Transactional
public class UserServiceV4 {

    private final UserRepositoryV4 userRepositoryV4;

    public UserServiceV4(EntityManager em) {
        this.userRepositoryV4 = new UserRepositoryV4Impl(em);
    }

    public Long join(User user){
        return userRepositoryV4.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV4.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
```

**UserServiceV4Test.java**
```java
@SpringBootTest
@Transactional
public class UserServiceV4Test {

    @Autowired EntityManager em;

    @Test
    @DisplayName("V4 service test")
    void v4_서비스_테스트(){
        User user=createUser("hong","123");

        UserServiceV4 userServiceV4=new UserServiceV4(em);
        Long saveId = userServiceV4.join(user);

        User findUser = userServiceV4.findOne(saveId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
```

**동작확인**

<img width="50%" alt="스크린샷 2021-10-01 오전 1 59 15" src="https://user-images.githubusercontent.com/56334761/135499077-0f396bb8-d51f-4951-8af4-15c64e7abcfe.png">

**문제점**

**`userServiceV4` 클래스를 보시면 여전히 구체적인 클래스에 종속되고 있다는 것을 보실 수 있습니다!**
```java
public UserServiceV4(EntityManager em) {
        this.userRepositoryV4 = new UserRepositoryV4Impl(em);
    }
```
흠.. 그렇다면 클래스 이름을 넣어서 오브젝트를 만들지 않으면 어떻게 사용할 수 있을까요..

### 🔧 Version 5.

먼저 Ver 4. 에서의 문제를 보면 `userService` 와 `userService`가 사용할 `userRepository`
의 특정 구현 클래스 사이의 관계를 설정해주는 것에 대한 관심이 잔존하고 있다는 문제가 있었습니다.

이 관심사를 담은 코드를 분리하지 않으면 독립적으로 확장 가능한 클래스가 될 수 없을 것입니다.

**해결 방법**
- 클라이언트 오브젝트의 활용
  - 두 개의 오브젝트가 있고, 한 오브젝트가 다른 오브젝트를 사용한다면 사용하는 쪽을 클라이언트 오브젝트, 사용되는 쪽을 오브젝트 서비스라고 합니다.
  - 👍 이를 활용하여 클라이언트 오브젝트에서 `service` 와 `repository`의 관계를 결정해주는 기능을 분리해 두면 됩니더.

**자세히**
- 클래스 사이에 관계가 만들어진다는 것은 한 클래스가 인터페이스 없이 다른 클래스를 직접 사용한다는 뜻.
- 따라서 클래스가 아니라 오브젝트와 오브젝트 사이의 관계를 설정해줘야 합니다.
  - 오브젝트 사이의 관계는 런타임 시에 한쪽이 다른 오브젝트의 레퍼런스를 갖고 있는 방식으로 만들어 집니다.
- `service` 오브젝트가 `repository` 인터페이스와 관계를 맺으려면 인터페이스르 구현한 오브젝트가 있어야 할텐데, 이를 굳이 `service`오브젝트에서 해주는 
것이 아니라고 이해하면 되겠습니다. 
  - 즉, 클라이언트 오브젝트에서 메소드 파라미터 등을 통해서 구현된 오브젝트를 제공해주기만 하면 됩니다.
  - 또한 오브젝트를 제공받는 쪽은 생성자, 수정자, 일반 메소드등을 통해서 전달 받으면 됩니다.
  - **자바의 다형성**이라는 특징을 잘 활용하여 오브젝트를 전달 받는 오브젝트에서의 생성자의 파라미터는 인터페이스 자체를 제공 받으면 되고,
  제공하는 쪽에서 인터페이스를 구현한 오브젝트를 제공해주면됩니다.

**위의 특징들을 이용하여 이제는 관심사의 분리가 완벽해 졌으며, 진정으로 변경과 확장이 용이해졌습니다.**
- 구체적인 오브젝트에 의존도 하지 않고,
- 인터페이스를 사용했기 때문에 제공받은 오브젝트는 정의된 메소드만 이용하면 되기 때문에 제공받은 오브젝트가 어떤 클래스로 부터 만들어졌는지도 신경 안써도 됩니다.

**Code**

**UserRepositoryV5.java**
```java
public interface UserRepositoryV5 {

    Long save(User user);
    Optional<User> findById(Long userId);
    void remove(Long userId);
}

```

**UserRepositoryV5Impl.java**
```java
@RequiredArgsConstructor
public class UserRepositoryV5Impl implements UserRepositoryV5 {

    private final EntityManager em;

    @Override
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class,userId));
    }

    @Override
    public void remove(Long userId) {
        User findUser = findById(userId).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
        em.remove(findUser);
    }

}
```

**UserServiceV5.java**
```java
@Transactional
public class UserServiceV5 {

    private final UserRepositoryV5 userRepositoryV5;

    public UserServiceV5(UserRepositoryV5 userRepositoryV5) {
        this.userRepositoryV5 = userRepositoryV5;
    }

    public Long join(User user){
        return userRepositoryV5.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV5.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
```

**AppConfig.java**
```java
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
```
`AppConfig`를 클라이언트 오브젝트라고 생각하시면 됩니다.
`service` 클래스의 생성자를 보시면
```java
public UserServiceV5(UserRepositoryV5 userRepositoryV5) {
        this.userRepositoryV5 = userRepositoryV5;
    }
```
구체적인 오브젝트에 의존하지 않는 것을 보실 수 있습니다.

**Test**
```java
@SpringBootTest
@Transactional
public class V5Test {

    @PersistenceContext EntityManager em;

    @Test
    @DisplayName("v5 repositoryTest")
    void v5_repository_테스트(){
        User user=createUser("hong","1234");

        UserRepositoryV5 userRepository=new UserRepositoryV5Impl(em);

        Long saveId= userRepository.save(user);

        User findUser=userRepository.findById(saveId).get();

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());

    }

    @Test
    @DisplayName("v5 total test")
    void v5_통합_테스트(){
        User user=createUser("hong","123");

        AppConfig appConfig=new AppConfig(em);
        UserServiceV5 userService= appConfig.userService();

        Long saveId = userService.join(user);

        User findUser = userService.findOne(saveId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }



    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
```

**동작 확인**

<img width="50%" alt="스크린샷 2021-10-01 오전 2 46 10" src="https://user-images.githubusercontent.com/56334761/135505200-cdf00341-5d64-4d37-abd0-d1276758948b.png">

**Ver 1. 에서 Ver 5. 에 걸쳐 서로 영향을 주지 않으면서도 필요에 따라 자유롭게 확장할 수 있는 구조를 완성 했습니다!!!**

### 🔍 원칙과 패턴
코드를 개선해온 결과를 객체지향 기술의 여러 가지 이론을 통해 알아봅시다.

**개방 폐쇄 원칙 (OCP, Open-Closed Principle)**
- '클래스나 모듈은 확장에는 열려 있어야 하고 변경에는 닫혀 있어야 한다.'
  - `service`에서 `repository`의 기능은 언제든지 확장이 가능한 상태입니다. 동시에 확장을 하더라도, `service` 자신의 핵심
   기능을 구현한 코드는 그런 변화에 영향을 받지 않고 유지할 수 있으므로 변경에는 닫혀 있다고 할 수 있습니다.
- 잘 설계된 객체지향 클래스의 구조를 살펴보면 OCP를 아주 좋게 지키고 있습니다.

**높은 응집도와 낮은 결합도**

개방 폐쇄 원칙은 높은 응집도와 낮은 결합도라는 소프트웨어 개발의 고전적인 원리로도 설명 가능합니다.
응집도가 높다는 것은 하나의 모듈, 클래스가 하나의 책임 또는 관심사에만 집중되어 있다는 것을 뜻하고, 
불필요하거나 직접 관련이 없는 외부의 관심과 책임이 얽혀 있지 않다는 뜻입니다.

- **높은 응집도**
  - 변화가 일어날 때 해당 모듈에서 변하는 부분이 크다는 것을 의미. 즉, 변경이 일어날 때 모듈의 많은 부분이 함께 바뀐다면 응집도가 높다고 말할 수 있습니다.
    - 모듈의 일부분만 변경이 일어난다면, 어떤 부분이 변경 되었는지 파악하기 어렵고, 그 변경으로 인해 다른 부분에 영향을 끼칠 수 도 있습니다.
  - 작업은 항상 전체적으로 일어나고 무엇을 변경할지 명확하며, 변경으로 인해 다른 클래스의 수정을 요구하지 않고, 기능에도 영향을 주지 않을 수 있습니다.
- **낮은 결합도**
  - 책임과 관심사가 다른 오브젝트 또는 모듈과 낮은 결합도, 즉 느슨하게 연결된 형태를 유지하는 것이 바람직 합니다.
  - '결합도'란 하나의 오브젝트가 변경이 일어날 때에 관계를 맺고있는 다른 오브젝트에게 변화를 요구하는 정도를 말합니다.

- 즉, `repository`라는 관심사를 하나의 관심사로 분리 하였으며, 인터페이스를 활용하였기 때문에 응집도가 높고, 결합도가 낮다고 할 수 있습니다.

**전략 패턴**

전략패턴은 자신의 기능에서, 필요에 따라 변경이 필요한 알고리즘을 인터페이스를 통해 통째로 외부로 분리시키고, 이를 구현한 구체적인 알고리즘 클래스를 필요에 따라 바꿔서
사용할 수 있는 디자인 패턴을 말합니다.
즉, 컨텍스트(`service`)를 사용하는 클라이언트(`AppConfig`)는 컨텍스트가 사용할 전략(`repository`)를 컨텍스트의 생성자등을 통해 제공해주는 것을 말합니다.

**지금 까지 개선해온 코드가 OCP, 높은 응집도, 낮은 결합도, 전략 패턴을 잘 지켰음을 증명했습니다.**

**스프링이란 객체지향적 설계 원칙과 디자인 패턴에 나타난 장점을 자연스럽게 개발자들이 활용할 수 있게 해주는 프레임워크입니다.**

***
## 🚀 제어의 역전(IOC)

### 🔧 팩토리
객체의 생성 방법을 결정하고 그렇게 만들어진 오브젝트를 돌려주는 일을 하는 오브젝트를 뜻합니다.
오브젝트를 생성하는 쪽과 생성된 오브젝트를 사용하는 쪽의 역할과 책임을 깔끔하게 분리하려는 목적으로 사용합니다.

이는, Ver 5. 에서 만들어봤던 `AppConfig` 와 같은 클래스를 뜻합니다.

```java
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
```

`service` -> `repository` 와 같은 의존 관계를 형성하고 있을 때, 새로운 `repository` 구현 클래스로 변경이 필요하면 `AppConfig`클래스만 수정해주면 됩니다.

예를 들자면, `repositoryImplV1` 과 `repositoryImplV2`와 같은 `repository`구현체가 있을 때,
```java
@RequiredArgsConstructor
public class AppConfig {

    private final EntityManager em;

    public UserRepositoryV5 userRepository(){
       // return new UserRepositoryV5Impl(em);
      return new UserRepositoryV5ImplV2(em);
    }

    public UserServiceV5 userService(){
        return new UserServiceV5(userRepository());
    }
}
```
이런 식으로만, 바꿔준다면 되는 것을 의미합니다.

### 🔧 제어권의 이전을 통한 제어관계 역전

일반적인 프로그램의 흐름은 main() 메소드와 같은 프로그램이 시작되는 지점에서 사용할 오브젝트, 오브젝트의 메소드 호출 등등 
모든 종류의 작업을 사용자가 능동적으로, 직접 제어하는 구조입니다.

**제어의 역전이란 이런 일반적인 프로그램의 흐름을 거꾸로 뒤집는 것 입니다.**

제어의 역전에서는 자신이 사용할 오브젝트를 결정하지 않을 뿐더럴 생성조차 하지 않습니다.
모든 제어 권한을 자신이 아닌 다른 대상에게 위임하는 것을 뜻합니다.

흔히, 스프링과 같은 프레임워크는 제어의 역전 개념이 적용된 대표적인 기술이라고 합니다.
프레임워크와 라이브러리를 혼동할수도 있는데 둘은 엄연히 차이가 있습니다.
- 라이브러리
  - 동작하는 중에 필요한 기능이 있을 때, 능동적으로 라이브러리를 사용. 애플리케이션의 흐름은 직접 제어
- 프레임워크
  - 애플리케이션 코드가 프레임워크에 의해 사용.
  - 프레임워크 위에 개발한 클래스를 등록해두고, 프레임워크가 흐름을 주도하는 중에 개발자가 만든 애플리케이션 코드를 사용하는 방식.

지금, 우리가 만든 `repository` 와 `service` 사이에서도 제어의 역전이 적용 되어 있습니다.
일반적인 프로그램의 경우, `repository`의 구현 클래스를 결정하고 오브젝트를 만드는 결정권한은 `service`에 있을 것입니다.
그러나, 지금음 `AppConfig`에 있습니다. `service`는 능동적인 존재가 아닌, 수동적인 존재가 되었다고고 말할 수 있습니다.
`service`는 자기 자신도 수동적으로 만들어 지며, 사용할 오브젝트 또한 수동적으로 공급 받게 됩니다.

현재 지금 만든 것은 가장 단순한 IOC 프레임워크를 만들었다고 보면 될것이고, 이제 애플리케이션 전반에 걸쳐 사용하기 위해서는 대표적인 IOC 프레임워크인
**스프링의 도움이 필요하합니다.(컴포넌트의 생성과 관계설정, 사용, 생명주기 관리등을 관장하는 존재)**

***

## 🚀 스프링 IOC

스프링의 핵심을 담당하는 것은 빈 팩토리 또는 애플리케이션 컨텍스트라고 불리는 것입니다. 한번 알아보죠~!

### 🔧 오브젝트 팩토리를 이용한 스프링 IOC

- 스프링 빈
  - 스프링에서는 스프링이 제어권을 가지고 직접 만들고 관계를 부여하는 오브젝트를 빈이라고 부릅니다. 또한, 스프링 컨테이너가 생성과 관계설정, 사용들을 제어해주는 
  제어의 역전이 적용된 오브젝트를 가리키는 말입니다.

**애플리케이션 컨텍스트, 빈 팩토리**
- 빈의 생성과 관계설정 같은 제어를 담당하는 IOC 오브젝트를 빈 팩토리 또는 애플리케이션 컨텍스트라고 부릅니다.
- 별도의 설정 정보(오브젝트를 어떻게 생성하고, 어떤 의존관계를 맺어주고)를 참고해서 빈의 생성, 관계설정 등의 제어 작업을 총괄합니다.

설정정보를 만드는 방법은 여러가지가 있는데,

**AppConfig.java**
```java
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
```
이러한 (롬복을 사용하긴 했지만?) 순수 자바 코드도, 스프링 애노테이션을 활용하 간단하게 설정정보를 만들어 줄 수 있습니다.

**설정 정보 만드는 방법**
1. 애플리케이션 컨텍스트를 위한 오브젝트 설정을 담당하는 클래스라고 인식할수 있도록 `@Configuration`이라는 어노테이션을 추가합니다.
2. 오브젝트를 만들어주는 메소드에는 `@Bean`이라는 애노테이션을 추가합니다.

**AppConfig.java**
```java
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
```

>현재, 이 설정 정보에서는 EntityManager Bean을 직접 만들어줬습니다. 헌재까지는, 스프링 부트에서 자동 주입하여 주는 EntityManager를 사용해주었습니다., 지금 
> 이 설정정보 클래스에서는 아직 스프링 부트에서 만든 EntityManager Bean이 존재하지 않기 때문에 따로 EntityManager Bean과 TransactionManager Bean을
> 만들어야합니다. 저는 Transaction이 존재하지 않아도 테스트를 수행하는데는 무리가 없다 생각하여, EntityManager만 생성해 주었고
> 스프링 애플리케이션이 로드될때, 스프링 부트에서 만드는 EntityManager와 혼동이 생길 수 있으므로 @Primary 애노테이션을 추가로 붙여 주었습니다.

**persistence.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.2"
             xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_2.xsd">
    <persistence-unit name="hello">
        <properties>
            <!-- 필수 속성 -->
            <property name="javax.persistence.jdbc.driver" value="org.h2.Driver"/>
            <property name="javax.persistence.jdbc.user" value="sa"/>
            <property name="javax.persistence.jdbc.password" value=""/>
            <property name="javax.persistence.jdbc.url" value="jdbc:h2:tcp://localhost/~/dependencytest"/>
            <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>

            <!-- 옵션 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.use_sql_comments" value="true"/>
            <!--<property name="hibernate.hbm2ddl.auto" value="create" />-->
        </properties>
    </persistence-unit>
</persistence>
```

이렇게, 애플리케이션 컨텍스트가 Ioc방식의 기능을 제공할 때 사용할 완벽한 설정정보를 만들었습니다.

이제, 테스트 코드를 통해 ApplicationContext를 만들어봅시다.
1. AnnotationConfigApplicationContext를 이용하여 설정정보를 적용한 애플리케이션 컨텍스트 생성
2. getBean() 메소드를 활용하여 설정정보에 정의해 놓은 Bean을 가져올 수 있습니다.

**SpringAppConfigTest.java**
```java
@SpringBootTest
@Transactional
public class SpringAppConfigV1Test {

    @Test
    @DisplayName("Bean Test")
    void 빈_테스트(){
        User user=createUser("hong","123");

        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);

        UserServiceV5 userService= ac.getBean("userService",UserServiceV5.class);

        Long saveId = userService.join(user);

        User findUser = userService.findOne(saveId);

        Assertions.assertThat(findUser.getName()).isEqualTo(user.getName());
        Assertions.assertThat(findUser.getPassword()).isEqualTo(user.getPassword());
    }

    private User createUser(String name, String password){
        return User.createUser()
                .name(name)
                .password(password)
                .build();
    }
}
```

**동작 확인**

<img width="50%" alt="스크린샷 2021-10-04 오후 10 51 11" src="https://user-images.githubusercontent.com/56334761/135863453-16dabc5d-9450-468e-9d3c-722aea2d98bc.png">

동작 확인 까지 하였습니다. 그런데 이렇게 봐서는 더 번거로울 뿐이지 딱히 장점은 없어보입니다.

이러한 고민은, 스프링은 날려버리라고 합니다. 얻을 수 없는 방대한 기능을 제공할테니..

### 🔧 애플리케이션 컨텍스트의 동작방식

오브젝트 팩토리와 애플리케이션 컨텍스트

사용 방식 및 설정 정보 먼저 보시죠.

**오브젝트 팩토리**

```java
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
```
```java
AppConfig appConfig=new AppConfig(em);
UserServiceV5 userService= appConfig.userService();
```

**애플리케이션 컨텍스트**
```java
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
```
```java
AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);
UserServiceV5 userService= ac.getBean("userService",UserServiceV5.class);

```

여기서 보시면, 오브젝트 팩토리는 `service` 오브젝트를 생성하고 `repository`와 관계를 맺어주는 제한적인 역할을 하는 데에 반해,
애플리케이션 컨텍스트는 IOC를 적용해서 관리할 모든 오브젝트에 대한 생성과 관계설정을 담당합니다.

애플리케이션 컨텍스트는 @Configuration 이 붙은 설정 정보를 활용하여 등록 된 빈을 호출해서 가져온 것을 클라이언트가 getBean()을 요청할 때 
전달해 줍니다.

애플리케이션 컨텍스트를 사용하는 이유는 범용적이고 유연한 방법으로 Ioc기능을 확장하기 위해서 입니다. 이렇게만 해서는 아직 까지도 장점이 뭔지
감이 안잡힙니다. 자세히 살펴보죠.

**애플레킹션 컨텍스트를 사용했을 때의 장점**
- 클라이언트는 구체적인 팩토리 클래스를 알 필요 없다.
  - 애플리케이션 발전시, IoC를 적용한 오브젝트도 계속 추가될 것, 클라이언트가 필요한 오브젝트를 가져오려면 어떤 팩토리 클래스를 사용 했는지를 알아야 하고,
  필요할 때 마다 팩토리 오브젝트를 생성해야하는 번거로움이 있습니다. (`AppConfig ac=new AppConfg()`, 새로운 팩토리인 DaoFactory가 만들어 졌다면
  `DaoFactory da=new DaoFactory()` 이렇게 구체적으로 알아야 함.)
  - 애플리케이션 컨텍스트를 활용하게 되면 일관된 방식으로 원하는 오브젝트를 가져올 수 있습니다.(`AnnotaionConfigApplicationContext ac=new Annotaion
  ConfigApplication(@Configuration이 붙은 클래스)`)
- 애플리케이션 컨텍스트는 종합 IoC 서비스를 제공.
  - 의존관계를 맺어주는 것 이상으로 오브젝트가 만들어지는 방식, 시점과 전략 등등 효과적인 다양한 기능들을 제공합니다.
- 애플리케이션 컨텍스트는 빈을 검색하는 다양한 방법을 제공
***
## 🚀 싱글톤 레지스트리와 오브젝트 스코프

### ✏️ 오브젝트의 동일성과 동등성
자바에서는 두 개의 오브젝트가 완전히 같은 동일한 오브젝트라 말하는 것과, 동일한 정보를 담고있는 오브젝트라고 말하는 것은 분명한 차이가 있습니다.
전자는 동일성비교라고 하고, 후자를 동등성 비교라고 합니다.

- 동일성
  - 두 개의 오브젝트가 동일하다면 사실은 하나의 오브젝트만 존재하는 것이고 두 개의 오브젝트 레퍼런스 변수를 갖고 있을 뿐
- 동등성
  - 두 개의 각기 다른 오브젝트가 메모리상에 존재하는 것, 변수 명이 같다거나 등등 적절한 동등성 기준을 두고 판단

**스프링 애플리케이션 컨텍스트와 순수 자바 코드로 만든 오브젝트 팩토리의 동일성을 판별해봅시다.**

**오브젝트 팩토리**

```java
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
```

**애플리케이션 컨텍스트**
```java
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
```

**테스트 코드**

```java
@Transactional
public class SingletonTest {

    @Autowired
    EntityManager em;


    @Test
    @DisplayName("자바 코드로 생성한 팩토리 테스트")
    void 자바_오브젝트_테스트(){
        AppConfig appConfig=new AppConfig(em);
        UserServiceV5 userService1=appConfig.userService();
        UserServiceV5 userService2=appConfig.userService();

        Assertions.assertThat(userService1).isNotEqualTo(userService2);
    }

    @Test
    @DisplayName("스프링 빈 애플리케이션 컨텍스트 테스트")
    void 스프링_빈_테스트(){
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);

        UserServiceV5 userService1=ac.getBean("userService",UserServiceV5.class);
        UserServiceV5 userService2=ac.getBean("userService",UserServiceV5.class);

        Assertions.assertThat(userService1).isEqualTo(userService2);
    }
}
```

**결과확인**

<img width="50%" alt="스크린샷 2021-10-13 오후 9 02 05" src="https://user-images.githubusercontent.com/56334761/137128721-1efa7c32-af8c-4ec5-87d4-540228ddf82a.png">

결과를 보시면 자바 코드로 생성한 팩토리 클래스는 매번 생성시 **동일성을 충족하지 않았고**,
스프링 빈 애플리케이션 컨텍스트는 **동일성을 충족**했다는 것을 알 수 있습니다.

### 🔧 싱글톤 레지스트리로서의 애플리케이션 컨텍스트

먼저 공부했을때 애플리케이션은 IOC컨테이너라는 것을 알 수 있었습니다.
추가로 애플리케이션 컨텍스트는 싱글톤을 저장하고 관리하는 싱글톤 레지스트리입니다.

스프링은 기본적으로 별다른 설정을 하지 않으면 내부에서 생성하는 빈 오브젝트를 모두 싱들톤으로 만듭니다.

**서버 애플리케이션과 싱글톤**

**근본적인 질문! 그렇다면 스프링은 왜 싱글톤으로 빈을 생성할까?**

스프링이 처음 설계됐던 이유로부터 비롯됩니다. 스프링은 초당 엄청난 수의 요청을 처리할 수 있는 높은 성능이 요구되며, 하나의 요청을 처리하기 위해
 다양한 기능을 담당하는 무수히 많은 오브젝트들이 계층형으로 이루어져 있습니다.

그런데 매번 클라이언트의 요청이 올 때마다 각 로직을 담당하는 오브젝트를 새로만들어서 사용한다면 엄청난 부하가 걸리게 되겠죠?!

**그렇기에, 스프링은 싱글톤으로 빈을 생성하게 됩니다.**

- 싱글톤 패턴의 원리
  - 애플리케이션 안에 제한된 수, 대개 한 개의 오브젝트만 만들어서 사용하는 것

**하지만, 싱글톤 패턴을 사용하기는 까다롭고 여러가지 문제가 있다고 합니다.**

- 자바에서 싱글톤을 구현하는 방법
  1. 다른 곳에서는 오브젝트를 생성하지 못하도록 private로 접근을 제한
  2. 생성된 싱글톤 오브젝트를 저장할 수 있는 자신과 같은 타입의 스태틱 필드를 정의
  3. 한 번 오브젝트가 만들어지고 난 뒤에는 메소드를 통하여 이미 만들어져 스태틱 필드에 저장해둔 오브젝트를 넘겨줌

**싱글톤 패턴의 한계**
1. private 생성자를 갖고 있기 때문에 상속 불가.
2. 테스트하기가 어려워짐
   - 싱글톤은 초기화 과정에서 생성자등을 통해 사용할 오브젝트를 다이내믹하게 주입하기가 힘듬
3. 서버환경에서는 싱들톤이 하나만 만들어지는 것을 보장하지 못함
   - 서버에서 클래스 로더를 어떻게 구성하고 있느냐에 따라서 싱글톤 클래스임에도 하나 이상의 클래스가 만들어질 수 있음
4. 싱글톤의 사용은 전역 상태를 만들 수 있음
   - 스태틱 필드이기 때문에 언제든지 접근이 용이해짐. 즉, 아무 객체나 자유롭게 접근하고 수정이 가능해집니다.

**싱글톤 레지스트리**

이런 한계에도 불가 하고 스프링은 싱글톤 사용을 적극 권장합니다. 한계를 극복하기 위해 스프링이 직접 싱글톤형태의 오브젝트를 만들고 관리하는 기능을 제공합니다.
그것을 바로 **싱글톤 레지스트리**라고 부릅니다.

싱글톤 레지스트리는 스태틱 필드와 private 생성자를 사용하지 않고 평범한 자바 클래스를 싱글톤으로 활용할 수 있게 해줍니다.
덕분에 싱글톤 방식으로 사용됭 애플리케이션 클래스라도 public 생성자를 가질 수 있습니다.

### 🔧 싱글톤과 오브젝트의 상태

싱글톤은 멀티스레드 환경이라면 여러 스레드가 동시에 접근해서 사용할 수 있습니다. 따라서 상태관리에 주의를 기울여야 합니다.
멀티스레드 환경에서 서비스 형태의 오브젝트로 사용되는 경우에는 상태정보를 내부에 갖고 있지 않은 무상태 방식으로 만들어져야 합니다.

왜냐하면, 싱글톤으로 유지시 저장할 공간은 하나 뿐이니 원치않은 값을 읽을 수 도 있고, 덮어 쓸수도 있기 때문입니다.
물론, 읽기전용의 값이라면 초기화 시점에서 인스턴스 변수에 저장해두고 공유하는 것은 아무런 문제가 없습니다.

그렇다면, 각 요청에 대한 정보나, 리소스로부터 생성한 정보가 필요한 경우는?? 이떄는 파라미터, 로컬 변수, 리턴 값등을 사용하면 됩니다.

***

## 🚀 의존관계 주입(DI)

### 🔧 런타임 의존관계 설정

의존관계란 무엇일까요??

두 개의 클래스 또는 모듈이 의존관계에 있다고 말할 때는 항상 방향성을 부여해야합니다.
즉, 누가 누구에게 의존하는 관계에 있다는 식이어야 합니다.

**ex) A 클래스 -> B 클래스 라는 의존관계가 있다고 생각해봅시다.**

여기서는 B가 변하면 A에게 영향을 미친다는 뜻입니다. B의 기능이 추가되거나 변경된다면 그 영향이 A로 전달된다는 뜻입니다.
대표적으로 A가 B를 사용하는 경우, A에서 B에 정의된 메소드를 호출해서 사용하는 경우를 `사용데 대한 의존관계`라고 합니다.

**`service`클래스의 의존관계**

**UserRepositoryV5.java**
```java
public interface UserRepositoryV5 {

    Long save(User user);
    Optional<User> findById(Long userId);
    void remove(Long userId);
}

```

**UserRepositoryV5Impl.java**
```java
@RequiredArgsConstructor
public class UserRepositoryV5Impl implements UserRepositoryV5 {

    private final EntityManager em;

    @Override
    public Long save(User user) {
        em.persist(user);
        return user.getId();
    }

    @Override
    public Optional<User> findById(Long userId) {
        return Optional.ofNullable(em.find(User.class,userId));
    }

    @Override
    public void remove(Long userId) {
        User findUser = findById(userId).orElse(null);
        if(findUser==null)
            throw  new IllegalArgumentException();
        em.remove(findUser);
    }

}
```

**UserServiceV5.java**
```java
@Transactional
public class UserServiceV5 {

    private final UserRepositoryV5 userRepositoryV5;

    public UserServiceV5(UserRepositoryV5 userRepositoryV5) {
        this.userRepositoryV5 = userRepositoryV5;
    }

    public Long join(User user){
        return userRepositoryV5.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV5.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
```

그럼 초기에 만들었던 `service` 클래스와 `repository`인터페이스간의 예를 봅시다.
`service` 클래스는 `repository`를 의존하고 있는 형태입니다.
따라서 `repository` 인터페이스가 변하게된다면, `service`가 직접적인 영향을 받게 됩니다.
하지만, 인터페이스를 구현하고 있는 `repositoryImpl` 클래스가 변하게 된다면, 받는 영향은 없습니다.

이렇게, 인터페이스에 대해서만 의존관계를 만들어두면 인터페이스 구현 클래스와의 관계는 느슨해지면서 변화에 영향을 덜 받는 상태가 됩니다.

`service` 클래스는 `repository`인터페이스에게만 의존한다고 볼 수 도 있는 형태입니다.
`service`는 구현체의 존재도 알지 못하는 상황입니다.

그런데, 모델이나 코드에서 클래스와 인터페이스를 통해 드러나는 의존관계 말고, 런타임 시에 오브젝트 사이에서 만들어지는 의존관계도 있습니다.
이를 `런타임 의존관계`라고 부릅니다. 즉, 설계 시점의 의존관계가 실체화 되는 것이라고 보시면 됩니다.

프로그램이 시작되고 `service` 오브젝트가 만들어지고 나서 런타임 시에 의존관계를 맺는 대상, 즉 실제 사용대상인 오브젝트를 의존 오브젝트라고 말합니다.

**런타임 시점 의존관계**
- 컨테이너나 팩토리 같은 제3의 존재가 결정합니다.
  - 설계 시점에서는 알지 못했던 두 오브젝트의 관계를 맺도록 (`service`와 `repositoryImpl`) 도와주는 제 3의 존재
  - DI에서 말하는 제3의 존재는 바로 관계설정 책임을 가진 코드를 분리해서 만들어지는 오브젝트 
    - 스프링 애플리케이션 컨텍스트, IOC컨테이너
- 의존관계는 사용할 오브젝트에 대한 레퍼런스를 외부에서 제공해줌으로써 만들어집니다.
  
**AppConfig.java**
```java
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
```

위의 코드는 런타임 시점에 `service`가 사용할 `repository` 타입의 오브젝트를 결정하고 이를 생성한 후에 `service`의 파라미터로 주입해서
구현체와의 런타임 의존관계를 맺게 해줍니다.
또한, IoC방식으로 오브젝트의 생성과 초기화, 제공등의 작업등도 수행하고 있으므로 IoC/DI 컨테이너라고 많이 부릅니다.

DI컨테이너에 의해 런타임 시에 의존 오브젝트를 사용할 수 있도록 그 레퍼런스를 전달받는 과정이 마치 멕소드를 통해 DI 컨테이너가 `service`에 주입해주는 것과 같다고 해서
이를 ㅇ의존관계 주입이라고 부릅니다.

### 🔧 의존관계 검색과 주입

의존관계를 맺는 방법이 외부로부터의 주입이 아니라 스스로 검색을 이용하기 때문에 의존관계 검색이라고 불립니다.

의존관계 검색은 자신이 필요로 하는 의존 오브젝트를 능동적으로 찾습니다. 어떤 구현체를 사용할지는 결정하지 않습니다.
의존관계를 맺을 오브젝트를 결정하는 것과 오브젝트의 생성작업은 외부 컨테이너에게 IoC로 맡기지만, 이를 가져올 때는 메소드나 생성자를 통한
주입 대신 스스로 컨테이너에게 요청하는 방법을 사용합니다.

**UserServiceV6.java**

```java
@Transactional
public class UserServiceV6 {
    private final UserRepositoryV5 userRepositoryV5;

    public UserServiceV6() {
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(SpringAppConfigV1.class);
        this.userRepositoryV5 = ac.getBean("userRepository",UserRepositoryV5.class);
    }

    public Long join(User user){
        return userRepositoryV5.save(user);
    }

    public User findOne(Long userId){
        Optional<User> findUser=userRepositoryV5.findById(userId);

        return findUser.orElseThrow(()->{
                    throw new RuntimeException();
                }
        );
    }
}
```


**테스트**

```java
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
```

**동작 확인**

<img width="50%" alt="스크린샷 2021-10-14 오전 1 54 31" src="https://user-images.githubusercontent.com/56334761/137178611-5f3f0fef-2e0e-4362-8563-e5e782943a97.png">

코드를 보시면 외부로 부터 의존관계를 주입 받는 것이 아닌 스스로 IoC 컨테이너에게 요청을 합니다.

**의존관계 검색과 주입의 차이점**

- 검색 방식
  - 자신이 스프링의 빈일 필요가 없습니다.
  - 그냥 직접 new 로 생성해도 됩니다.
- 주입 방식
  - 자신도 반드시 빈이어야 합니다.
    - 컨테이너가 오브젝트를 주입하기 위해서는 생성과 초기화 권한을 갖고 있어야하기 때문입니다.

**의존관계 검색 방법은 적어도 한번이상은 쓰입니다**

애플리케이션이 시작되는 시점에서 스태틱 메소드인 main()은 DI를 이용하여 오브젝트를 주입받을 방법이 없기때문입니다.

서버에서도 의존관계 검색 방법이 사용됩니다. 사용자의 요청을 받을 때마다 DispatcherServlet 에서 스프링 컨테이너에 담긴 오브젝트를 사용하기 위해서 입니다.