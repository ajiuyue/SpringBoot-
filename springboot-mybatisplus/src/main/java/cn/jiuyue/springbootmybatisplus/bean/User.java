package cn.jiuyue.springbootmybatisplus.bean;

import lombok.Data;

/**
 * Create bySeptember
 * 2019/9/1
 * 16:48
 */
@Data
public class User {
    private Long id;
    private String name;
    private Integer age;
    private String email;
}