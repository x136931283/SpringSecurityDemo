package com.tianblogs.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tianblogs.security.entity.SysUser;

/**
 *
 */
public interface SysUserService extends IService<SysUser> {
    SysUser getByUsername(String username);

    String getUserAuthorityInfo(Long userId);

    SysUser getByAccount(String account);
}
