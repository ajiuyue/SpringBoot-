package cn.jiuyue.provider.service;

import cn.jiuyue.common.api.HelloService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * Create bySeptember
 * 2019/8/31
 * 21:11
 */
@Service(interfaceClass = HelloService.class)
@Component
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String message) {
        return "hello," + message;
    }
}
