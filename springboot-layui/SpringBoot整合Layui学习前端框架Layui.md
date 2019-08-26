
##### [经典模块化前端框架Layui官网链接](https://www.layui.com/)

SpringBoot整合前端框架 Layui,首先到Layui官网将模块化前端框架下载下来，找到示例的后[后台布局页面](https://www.layui.com/demo/admin.html)，我们简单搭建一个后台系统页面。

跟官方文档快速上手的，在idea新建一个SpringBoot项目，引入必要依赖。因为要用到thymeleaf模板引擎渲染数据，所以我们在pom.xml中将其引入。
```
    <dependencies>
        <!--thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!--web-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
    </dependencies>
```
 
- 将Layui下载到的文件中的 `layui.css`,`layui.js`复制到项目中的static文件夹。
- 在templates下新建一个index页面，将Layui后台布局的页面代码复制进去，将`layui.css`,`layui.js`资源引入。
```
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>layout 后台大布局</title>
    <link rel="stylesheet" href="/css/layui.css">
    <script src="/layui.js"></script>
    <script src="/common.js"></script>
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
    <!--header-->
    <div th:insert="~{header::header}"></div>
    <!--header end-->

    <!--side_nav-->
    <div th:insert="~{side_nav::side_nav}"></div>
    <!--side_nav end-->
    <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding: 15px;">内容主体区域</div>
    </div>

    <!--footer-->
    <div th:insert="~{footer::footer}"></div>
    <!--footer end-->

</div>
</body>
</html>
```
为了将header、side_nav、footer的代码复用，我们使用thymeleaf中的模板分片，将每个页面都有的提取出来复用，减少代码量。

前端控制器代码
```
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

```
源码地址：https://github.com/ajiuyue/SpringBoot-/tree/master/springboot-layui
