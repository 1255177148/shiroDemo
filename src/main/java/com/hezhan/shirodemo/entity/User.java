package com.hezhan.shirodemo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName(value = "user")
@Data
public class User {

    @TableId
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("password")
    private String password;

    @TableField("state")
    private Integer state;
}
