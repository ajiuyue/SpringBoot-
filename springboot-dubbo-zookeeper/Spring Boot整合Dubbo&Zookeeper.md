Dubbo是一款由阿里巴巴开发的远程服务调用框架（RPC），其可以透明化的调用远程服务，就像调用本地服务一样简单。Spring Boot项目整合Dubbo更为简单方便。Zookeeper在这里充当的是服务注册中心的角色，我们将各个微服务提供的服务通过Dubbo注册到Zookeeper中，然后服务消费者通过Dubbo从Zookeeper中获取相应服务并消费。本文案例的架构图可以简单用下图表示：
![image](F7BEE89B44014196B4967D899ED982DA)

本文案例最终项目结构如下图所示：

![image](7A42FC50FC814948984DB0375A6B5B84)

项目采用Maven构建，各模块的作用：

模块 | 描述
---|---
common-api | 统一定义接口，供其余子模块引用
server-provider | 服务提供者，实现common-api模块中的接口，然后暴露到Zookeeper中，供服务消费者使用
server-consumer | 服务消费者，通过Dubbo从Zookeeper中获取服务并消费

#### 环境准备

##### Zookeeper安装
在搭建项目之前需要启动Zookeeper服务，Zookeeper下载地址：http://zookeeper.apache.org/releases.html#download。

下载后解压，将config目录下的zoo_sample.cfg重命名为zoo.cfg(Zookeeper配置文件，默认端口为2181，可根据实际进行修改)。然后双击bin目录下的zkServer.cmd启动即可

##### 构建父模块
新建一个Maven项目，groupId为 cn.jiuyue，artifactId为dubbo-boot，packaging指定为pom。然后引入Spring Boot，dubbo-spring-boot-starter和Zookeeper相关依赖：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.jiuyue</groupId>
    <artifactId>dubbo-boot</artifactId>
    <!--打包方式pom-->
    <packaging>pom</packaging>
    <version>1.0</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/>
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <project.version>1.0</project.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- dubbo -->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>0.2.0</version>
        </dependency>
        <!-- zookeeper -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.8</version>
        </dependency>
        <dependency>
            <groupId>com.101tec</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.10</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

##### 构建Common-api

新建一个Maven模块，artifactId为common-api，目录结构如下所示：
![image](1C51B2FFDBA74ECCB6530A221AECDB3F)

###### pom.xml：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo-boot</artifactId>
        <groupId>cn.jiuyue</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>common-api</artifactId>
</project>
```

项目只包含一个HelloService接口：
```
package cn.jiuyue.common.api;

public interface HelloService {
    String hello(String message);
}

```

至此我们可以开始构建服务提供者和服务消费者了。

##### 构建Server-Provider

新建一个Maven模块，用于暴露Dubbo服务，artifactId为server-provider，目录结构如下所示：
![image](C6EBBB9271FF4EA694C2F60B955AD534)

###### pom内容如下：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo-boot</artifactId>
        <groupId>cn.jiuyue</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>server-provider</artifactId>

    <dependencies>
        <dependency>
            <groupId>cn.jiuyue</groupId>
            <artifactId>common-api</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>

</project>
```
这里我们引入了common-api模块，用于后续实现相应的服务。

在Spring Boot启动类中我们加入@EnableDubbo注解，表示要开启dubbo功能:
```
package cn.jiuyue.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//表示要开启dubbo功能
@EnableDubbo
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        System.out.println("complete");
    }
}

```
接着在application.yml中配置Dubbo：
```
server:
  port: 8081
dubbo:
  application:
    # 服务名称，保持唯一
    name: server-provider
    # zookeeper地址，用于向其注册服务
    registry:
      address: zookeeper://127.0.0.1:2181
    #暴露服务方式
    protocol:
      # dubbo协议，固定写法
      name: dubbo
      # 暴露服务端口 （默认是20880，不同的服务提供者端口不能重复）
      port: 20880


```
如果Zookeeper是集群的话，spring.dubbo.registry.address配置为：
```
spring:
  dubbo:
    registry:
      address: zookeeper://127.0.0.1:2181?backup=127.0.0.1:2180,127.0.0.1:2182
```

接下来我们在cc.mrbird.provider.service路径下创建一个HelloService接口的实现类：
```
package cn.jiuyue.provider.service;

import cn.jiuyue.common.api.HelloService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service(interfaceClass = HelloService.class)
@Component
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String message) {
        return "hello," + message;
    }
}

```
值得注意的是@Service注解为Dubbo提供的com.alibaba.dubbo.config.annotation.Service，而非Spring的那个。其中interfaceClass是指要发布服务的接口。

通过上面的配置，我们已经将HelloService接口的实现暴露到Zookeeper中了，接下来我们继续创建一个服务消费者，来消费这个服务。

##### 搭建Server-Consumer

新建一个Maven模块，用于消费Dubbo服务，artifactId为server-consumer，目录结构如下所示：
![image](779251E2FB4149ED950C4B8EBFAF75CF)

pom内容如下：
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>dubbo-boot</artifactId>
        <groupId>cn.jiuyue</groupId>
        <version>1.0</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>server-consumer</artifactId>

    <dependencies>
        <dependency>
            <groupId>cn.jiuyue</groupId>
            <artifactId>common-api</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>
```
同样的，我们也在Spring Boot启动类中我们加入@EnableDubbo注解，表示要开启dubbo功能。

接着在application.yml中配置Dubbo：
```
server:
  port: 8082

dubbo:
  application:
    # 服务名称，保持唯一
    name: server-provider
    # zookeeper地址，用于向其注册服务
  registry:
    address: zookeeper://127.0.0.1:2181
  #暴露服务方式
  protocol:
    # dubbo协议，固定写法
    name: dubbo
    # 暴露服务端口 （默认是20880，不同的服务提供者端口不能重复）
    port: 20880
```
同服务提供者，我们需要指定Zookeeper的地址，协议为dubbo。

接着我们定义一个TestController，演示服务消费：
```
@RestController
public class HelloController {
    @Reference
    private HelloService helloService;
    @GetMapping("/hello/{message}")
    public String hello(@PathVariable String message){
        return helloService.hello(message);
    }
}

```
通过Dubbo的@Reference注解注入需要使用的interface，类似于Spring的@Autowired。


consumer启动类,同样需要开启Dubbo
```
@SpringBootApplication
@EnableDubbo
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
        System.out.println(" ConsumerApplication complete");
    }
}

```
##### 测试
![image](F39AAA1D1B9148B3810245F9C54FACA8)

明远程服务调用已经成功。

这里只是通过Spring Boot和Dubbo的整合来简单了解Dubbo的使用，仅作抛砖引玉，更为详细的Dubbo配置可以查看官方文档：http://dubbo.apache.org/zh-cn/docs/user/quick-start.html

源码地址：https://github.com/ajiuyue/SpringBoot-/tree/master/springboot-dubbo-zookeeper/dubbo-boot