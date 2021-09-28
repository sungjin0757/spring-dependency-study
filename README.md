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