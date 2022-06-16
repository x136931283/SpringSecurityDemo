package com.tianblogs.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.lang.annotation.Target;

/**
 * @author tian
 */
@Data
public class SysUser {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String account;

    private String password;

    /**
     * 权限 比如ROLE_admin,ROLE_normal,sys:user:list,... 测试是使用
     * 正式需使用RBAC模型
     *
     */
    private String authority;

    private String name;

}
