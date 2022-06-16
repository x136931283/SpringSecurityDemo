package com.tianblogs.security.controller;

import com.tianblogs.security.constant.RsaProperties;
import com.tianblogs.security.entity.Result;
import com.tianblogs.security.entity.SysUser;
import com.tianblogs.security.handler.password.PasswordEncoder;
import com.tianblogs.security.service.SysUserService;
import com.tianblogs.security.utils.RSAUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用户控制器
 * @author tian
 *
 * login 和 logout 由框架管理
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public Result register(@RequestBody SysUser sysUser){
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        try {
            //查询当前账号是否存在
            SysUser sysUser1 = sysUserService.getByAccount(sysUser.getAccount());
            if (sysUser1 !=null){
                return Result.fail("当前账号已经存在，请更换账号");
            }
            //REA解密
            String pwd = RSAUtils.decryptByPrivate(sysUser.getPassword(), RsaProperties.privateKey);
            //SpringSecurity 密码编码
            String encodePwd = passwordEncoder.encode(pwd);
            sysUser.setPassword(encodePwd);
            sysUserService.save(sysUser);
            return Result.succ("注册成功！");

        }finally {
            lock.unlock();
        }

    }
}
