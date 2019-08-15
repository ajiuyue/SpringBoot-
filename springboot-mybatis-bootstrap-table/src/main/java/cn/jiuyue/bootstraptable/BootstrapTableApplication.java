package cn.jiuyue.bootstraptable;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.jiuyue.bootstraptable.mapper")
public class BootstrapTableApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootstrapTableApplication.class, args);
    }

}
