package com.tianblogs.security.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianblogs.security.entity.SysUser;
import com.tianblogs.security.mapper.SysUserMapper;
import com.tianblogs.security.service.SysUserService;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getName,username));
    }

    @Override
    public String getUserAuthorityInfo(Long userId) {
        SysUser sysUser = getById(userId);
        return sysUser.getAuthority();
    }

    @Override
    public SysUser getByAccount(String account) {
        return this.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getAccount,account));
    }
}
