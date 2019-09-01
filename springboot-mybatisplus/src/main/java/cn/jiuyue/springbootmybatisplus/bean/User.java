package cn.jiuyue.springbootmybatisplus.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * Create bySeptember
 * 2019/9/1
 * 16:48
 */
@Data
public class User {
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //乐观锁
    @Version
    private Integer version;
    //逻辑删除字段
    @TableLogic
    private Integer deleted;
}