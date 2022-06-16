package com.tianblogs.security.user;

import com.tianblogs.security.entity.SysUser;
import com.tianblogs.security.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  定义了UserDetails接口的实现类，我们就需要定义UserDetailsService接口的实现类，重写其loadUserByUsername方法，该方法需使用用户名在数据库中查找用户信息返回，返回值需封装成UserDetails。直接上代码：
 *
 *  实现了上述几个接口，从数据库中验证用户名、密码的过程将由框架帮我们完成，是封装隐藏了，所以不懂Spring Security的人可能会对登录过程有点懵，不知道是怎么判定用户名密码是否正确的。
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getByAccount(account);
        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误");
        }


        return new AccountUser(sysUser.getId(), sysUser.getName(), sysUser.getPassword(), getUserAuthority(sysUser.getId()));

    }

    /**
     * 获取用户权限信息（角色、菜单权限）
     * @param userId
     * @return
     */
    public List<GrantedAuthority> getUserAuthority(Long userId) {
    	// 实际怎么写以数据表结构为准，这里只是写个例子
        // 角色(比如ROLE_admin)，菜单操作权限(比如sys:user:list)
        String authority = sysUserService.getUserAuthorityInfo(userId);     // 比如ROLE_admin,ROLE_normal,sys:user:list,...

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}
