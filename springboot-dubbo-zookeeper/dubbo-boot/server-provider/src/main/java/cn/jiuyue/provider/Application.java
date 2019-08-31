package cn.jiuyue.provider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Create bySeptember
 * 2019/8/31
 * 21:05
 */
@SpringBootApplication
//表示要开启dubbo功能
@EnableDubbo
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        System.out.println("complete");
    }
}
