package cn.jiuyue.springbootlayui.controller;

import cn.jiuyue.springbootlayui.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Create bySeptember
 * 2019/8/26
 * 13:20
 */
@Controller
public class ItemController {
    @GetMapping("/list")
    public String list(Model model){
        User user1 =  new User();
        user1.setNikename("jiuyue1");
        user1.setEmail("jiuyue@163.com");
        user1.setPhone("1997858288");

        User user2 =  new User();
        user2.setNikename("jiuyue2");
        user2.setEmail("jiuyue@163.com");
        user2.setPhone("1997858288");

        User user3 =  new User();
        user3.setNikename("jiuyue3");
        user3.setEmail("jiuyue@163.com");
        user3.setPhone("1997858288");
        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        model.addAttribute("users",users);
        return "list";
    }
}
