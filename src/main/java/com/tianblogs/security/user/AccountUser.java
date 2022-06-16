package com.tianblogs.security.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 *  SpringSecurity中的认证管理器AuthenticationManager是一个抽象接口，用以提供各种认证方式。一般我们都使用从数据库中验证用户名、密码是否正确这种认证方式。
 *   AuthenticationManager的默认实现类是ProviderManager，ProviderManager提供很多认证方式，DaoAuthenticationProvider是AuthenticationProvider的一种实现，
 * 可以通过实现UserDetailsService接口的方式来实现数据库查询方式登录。
 *   UserDetailsService定义了loadUserByUsername方法，该方法通过用户名去查询出UserDetails并返回，UserDetails是一个接口，实际重写该方法时需要返回它的实现类
 *   Spring Security在拿到UserDetails之后，会去对比Authentication（Authentication如何得到？我们使用的是默认的UsernamePasswordAuthenticationFilter,
 * 它会读取表单中的用户信息并生成Authentication），若密码正确，则Spring Secuity自动帮忙完成登录


 * 我们需定义一个UserDetails接口的实现类,称为AccountUser。该实现类需要实现UserDetails接口的所有方法，自由度较低，只能按框架规定的来，我们来看看UserDetails接口的所有方法：
 *
 *
 *
 * 注意这里提供了两个构造函数，一是用户所有情况都正常，只需提供用户名、密码、权限、ID这几个参数，二是需提供所有参数的情况。一般我们都是使用第一种构造函数。
 *   现在我们发现Spring Security是默认我们的项目有权限管理需求的，因此必须重写getAuthorities方法返回用户的权限，这里也可返回空，不过之后的代码也不好写。
 * 所以Spring Security其实是和权限管理功能绑定的，需要在数据库中设计用户表、菜单表、权限表等等。若我们的项目比较简单，不需要复杂的权限管理功能，
 * 那么可以直接用Shiro框架，否则使用Spring Security将比较麻烦。
 */
public class AccountUser implements UserDetails {

    private Long userId;

    private static final long serialVersionUID = 540L;
    private static final Log logger = LogFactory.getLog(User.class);
    private String password;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public AccountUser(Long userId, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(userId, username, password, true, true, true, true, authorities);
    }

    public AccountUser(Long userId, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        Assert.isTrue(username != null && !"".equals(username) && password != null, "Cannot pass null or empty values to constructor");
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.accountNonExpired = accountNonExpired;
        this.credentialsNonExpired = credentialsNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
