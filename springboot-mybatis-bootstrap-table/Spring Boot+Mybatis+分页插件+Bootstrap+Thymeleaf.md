Bootstrap Table是一款基于Bootstrap的jQuery表格插件，通过简单的设置，就可以拥有强大的单选、多选、排序、分页，以及编辑、导出、过滤、扩展等等的功能。作者地址：https://github.com/wenzhixin/bootstrap-table。
#### 实现
在springboot中使用Bootstrap Table,首先我需要引入一些依赖：
```
 <dependencies>
        <!--web-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!--mysql-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>

        <!--mybatis-->
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>1.3.2</version>
        </dependency>

        <!--page helper分页起步依赖-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-starter</artifactId>
            <version>1.2.5</version>
        </dependency>
        <!--thymeleaf-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!--test-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
因为是web项目所以引入`spring-boot-starter-web`和`thymeleaf`,需要用到数据库所以引入`mysql-connector-java`和`mybatis-spring-boot-starter`，因为要分页所以直接用了Mybatis的一个分页`pagehelper`.

项目中使用Mybatis的逆向工程自动生成实体和访问数据库的Mapper成代码，所以需要在pom.xml文件中添加：
```
 <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.5</version>
                <configuration>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                </configuration>
                <!--mysql-->
                <dependencies>
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>5.1.47</version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
```

然后需要在resources下新建generatorConfig.xml，并复制下面代码：
```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="DB2Tables"  targetRuntime="MyBatis3">
        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin"></plugin>
        <commentGenerator>
            <!-- 不希望生成的注释中包含时间戳 -->
            <property name="suppressDate" value="true"/>
            <!-- 是否去除自动生成的注释 true：是 ： false:否 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <!--数据库链接URL，用户名、密码 -->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost/db_community"
                        userId="root"
                        password="1111">
        </jdbcConnection>
        <!-- 默认false，把JDBC DECIMAL 和 NUMERIC 类型解析为 Integer，
        为 true时把JDBC DECIMAL和NUMERIC 类型解析为java.math.BigDecimal -->
        <javaTypeResolver>
            <property name="forceBigDecimals" value="false"/>
        </javaTypeResolver>
        <!-- 生成模型的包名和位置-->
        <javaModelGenerator targetPackage="cn.jiuyue.bootstraptable.model" targetProject="src/main/java">
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>
        <!-- 生成映射文件的包名和位置-->
        <sqlMapGenerator targetPackage="mapper" targetProject="src/main/resources">
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>
        <!-- 生成dao(mapper)接口的包名和位置-->
        <javaClientGenerator type="XMLMAPPER" targetPackage="cn.jiuyue.bootstraptable.mapper" targetProject="src/main/java">
            <property name="enableSubPackages" value="false"/>

        </javaClientGenerator>
        <!-- 要生成的表 tableName是数据库中的表名或视图名 domainObjectName是实体类名-->
        <table tableName="comment" domainObjectName="Comment"></table>
    </context>
</generatorConfiguration>

```

安装图示操作完成代码的自动生成:
![微信图片_20190815173743.png](https://i.loli.net/2019/08/15/1OCrzl7tYVLGAfv.png)
###### 需要说明的是：
- `<plugin type="org.mybatis.generator.plugins.RowBoundsPlugin"></plugin>`,因为我们要使用分页查询的功能，而我又想直接在生成的代码中生成带有能够实现分页功能的函数接口，所以我们需要添加上面的插件代码。`
- 另外生成的XMapper接口还没有添加注解，一个方法是在每个Mapper接口类上注解`@Mapper`，还有一种更方便的方法就是在启动类中使用`@MapperScan("cn.jiuyue.bootstraptable.mapper")`。
```
@SpringBootApplication
@MapperScan("cn.jiuyue.bootstraptable.mapper")
public class BootstrapTableApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootstrapTableApplication.class, args);
    }
}
```
在application.yml文件配置的是：
```
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/db_community?characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
    username: root
    password: 1111
pagehelper:
  auto-dialect: mysql
  reasonable: true
  support-methods-arguments: true
  page-size-zero: true
server:
  port: 8088
mybatis:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
  type-aliases-package: cn.jiuyue.bootstraptable.model

````

编写一个简单的页面,因为基于Bootstrap，所以先引入Bootstrap依赖，然后引入Bootstrap Table依赖：
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Index</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <script src="http://code.jquery.com/jquery-2.0.0.min.js"></script>
    <script src="https://cdn.bootcss.com/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.9.0/bootstrap-table.min.css">
    <!-- Latest compiled and minified JavaScript -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.9.0/bootstrap-table.min.js"></script>
    <!-- Latest compiled and minified Locales -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.9.0/locale/bootstrap-table-zh-CN.min.js"></script>
</head>
<body>
    <form class="form">
        <div class="row">
            <div class="col">
                <div class="input-group">
                    <span class="input-group-addon">角色：</span>
                    <div class="form-group">
                        <input type="text" name="commentator" class="form-control">
                    </div>
                </div>
            </div>
            <div class="col"></div>
            <div class="col">
                <button type="button" class="btn btn-success" onclick="refresh()">重置</button>
                <button type="button" class="btn btn-primary" onclick="search()">搜索</button>
            </div>
        </div>
    </form>
    <table id="roleTable" data-mobile-responsive="true" class="mb-bootstrap-table text-nowrap"></table>

    <script>
        $('#roleTable').bootstrapTable({
            method: 'get', // 服务器数据的请求方式 get or post
            url: "/index", // 服务器数据的加载地址
            striped: true, //是否显示行间隔色
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true, //是否显示分页（*）
            sortable: false, //是否启用排序
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [5, 25, 50, 100], //可供选择的每页的行数（*）
            strictSearch: true,
            minimumCountColumns: 2, //最少允许的列数
            clickToSelect: true, //是否启用点击选中行
            uniqueId: "ID", //每一行的唯一标识，一般为主键列
            cardView: false,
            detailView: false, //是否显示详细视图
            smartDisplay: false,
            queryParams: function(params) {
                return {
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    roleName: $(".form").find("input[name='commentator']").val().trim(),
                };
            },
            columns: [{
                checkbox: true
            },{
                field: 'id',
                title: '评论Id'
            },{
                field: 'commentator',
                title: '评论人编号'
            }, {
                field: 'content',
                title: '评论内容'
            },{
                field: 'gmtCreate',
                title: '创建时间'
            }, {
                field: 'gmtModified',
                title: '修改时间'
            }]
        });
        // 搜索方法
        function search() {
            $('#roleTable').bootstrapTable('refresh');
        }
        // 重置方法
        function refresh() {
            $(".form")[0].reset();
            search();
        }
    </script>
</body>
</html>
```
完成controller接口：
```
public class BaseController {
    protected Map<String, Object> getDataTable(PageInfo<?> pageInfo) {
        Map<String, Object> rspData = new HashMap<String, Object>();
        rspData.put("rows", pageInfo.getList());
        rspData.put("total", pageInfo.getTotal());
        return rspData;
    }
}

```

```
@Controller
public class CommentController extends BaseController{

    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "list",method = RequestMethod.GET)
    @ResponseBody
    public PageInfo<Comment> list(Integer page, Integer size){
        page = 0;
        size = 10;
        PageInfo<Comment> list = commentService.list(page, size);
        return list;
    }
    @GetMapping("index")
    @ResponseBody
    public Map<String,Object> index(Integer page, Integer size, Model model){
        page = 0;
        size = 10;
        PageInfo<Comment> list = commentService.list(page, size);
        return getDataTable(list);
    }
    @GetMapping("/")
    public String index(){
        return "index";
    }
}

```
页面显示效果如下：
![微信图片_20190815175642.png](https://i.loli.net/2019/08/15/YU294iQolDJwsr1.png)

#### 附录：
Bootstap Table除了上面介绍的内容外，其还包含了许多别的特性，可参考官方文档：http://bootstrap-table.wenzhixin.net.cn/zh-cn/documentation/。

源码链接：https://github.com/ajiuyue/SpringBoot-/tree/master/springboot-mybatis-bootstrap-table