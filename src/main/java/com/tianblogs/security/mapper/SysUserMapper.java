package com.tianblogs.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tianblogs.security.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper  extends BaseMapper<SysUser> {
}
