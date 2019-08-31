package cn.jiuyue.consumer.controller;

import cn.jiuyue.common.api.HelloService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create bySeptember
 * 2019/8/31
 * 21:58
 */
@RestController
public class HelloController {
    @Reference
    private HelloService helloService;
    @GetMapping("/hello/{message}")
    public String hello(@PathVariable String message){
        return helloService.hello(message);
    }
}
