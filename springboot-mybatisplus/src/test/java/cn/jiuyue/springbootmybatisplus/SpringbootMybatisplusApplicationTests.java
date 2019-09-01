package cn.jiuyue.springbootmybatisplus;

import cn.jiuyue.springbootmybatisplus.bean.User;
import cn.jiuyue.springbootmybatisplus.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootMybatisplusApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        User user = new User();
        user.setAge(18);
        user.setName("aaabb");
        user.setEmail("163@163.com");
        int insert = userMapper.insert(user);
        System.out.println(insert);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(1L);
        user.setEmail("163s@163.com");
        int id = userMapper.updateById(user);
        System.out.println(id);
    }

    @Test
    public void testOptimisticLocker() {
        //取出记录时，获取当前version
        User user = userMapper.selectById(1L);
        //修改
        user.setName("乐观锁");
        //执行更新
        userMapper.updateById(user);
    }

    @Test
    public void testSelectByMap() {

        Map<String,Object> map = new HashMap<>();
        map.put("name","aaa");
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    @Test
    public void testLogicDelete() {
       userMapper.deleteById(1L);
    }
}
