#### 快速开始
我们将通过一个简单的 Demo 来阐述 MyBatis-Plus 的强大功能，在此之前，我们假设您已经：

- 拥有 Java 开发环境以及相应 IDE
- 熟悉 Spring Boot
- 熟悉 Maven
- 现有一张 User 表，其表结构如下：
```
id	name	age	email
1	Jone	18	test1@baomidou.com
2	Jack	20	test2@baomidou.com
3	Tom	28	test3@baomidou.com
4	Sandy	21	test4@baomidou.com
5	Billie	24	test5@baomidou.com
```
其对应的数据库 Schema 脚本如下：
```
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
	id BIGINT(20) NOT NULL COMMENT '主键ID',
	name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
	age INT(11) NULL DEFAULT NULL COMMENT '年龄',
	email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
	PRIMARY KEY (id)
);
```
其对应的数据库 Data 脚本如下：
```
DELETE FROM user;

INSERT INTO user (id, name, age, email) VALUES
(1, 'Jone', 18, 'test1@baomidou.com'),
(2, 'Jack', 20, 'test2@baomidou.com'),
(3, 'Tom', 28, 'test3@baomidou.com'),
(4, 'Sandy', 21, 'test4@baomidou.com'),
(5, 'Billie', 24, 'test5@baomidou.com');
```

#### 初始化工程
创建一个空的 Spring Boot 工程（工程将以 H2 作为默认数据库进行演示）

可以使用 Spring Initializer 快速初始化一个 Spring Boot 工程

#### 添加依赖
引入 Spring Boot Starter 父工程：
```
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.7.RELEASE</version>
    <relativePath/>
</parent>
```
引入 spring-boot-starter、spring-boot-starter-test、mybatis-plus-boot-starter、lombok、h2 依赖：
```
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.2.0</version>
    </dependency>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
</dependencies>
```
##### 配置
在 application.yml 配置文件中添加 Mysql 数据库的相关配置：
```
server.port=8088
# DataSource Config
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/db_test?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=1111
```
在 Spring Boot 启动类中添加 @MapperScan 注解，扫描 Mapper 文件夹：
```
@SpringBootApplication
@MapperScan("cn.jiuyue.springbootmybatisplus.mapper")
public class SpringbootMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootMybatisplusApplication.class, args);
    }

}
```
#### 编码
编写实体类 User.java（此处使用了 Lombok 简化代码）
```
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}
```
编写Mapper类 UserMapper.java
```
public interface UserMapper extends BaseMapper<User> {

}
```
#### 开始使用
添加测试类，进行功能测试：
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

}
```
UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper，所以不填写就是无任何条件

控制台输出：
```
User(id=1, name=Jone, age=18, email=test1@baomidou.com)
User(id=2, name=Jack, age=20, email=test2@baomidou.com)
User(id=3, name=Tom, age=28, email=test3@baomidou.com)
User(id=4, name=Sandy, age=21, email=test4@baomidou.com)
User(id=5, name=Billie, age=24, email=test5@baomidou.com)
```