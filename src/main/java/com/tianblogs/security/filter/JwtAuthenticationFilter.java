package com.tianblogs.security.filter;

import cn.hutool.core.util.StrUtil;
import com.tianblogs.security.entity.SysUser;
import com.tianblogs.security.service.SysUserService;
import com.tianblogs.security.user.UserDetailServiceImpl;
import com.tianblogs.security.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在首次登录成功后，LoginSuccessHandler将生成JWT，并返回给前端。在之后的所有请求中（包括再次登录请求），都会携带此JWT信息。
 * 我们需要写一个JWT过滤器JwtAuthenticationFilter，当前端发来的请求有JWT信息时，该过滤器将检验JWT是否正确以及是否过期，若检验成功，
 * 则获取JWT中的用户名信息，检索数据库获得用户实体类，并将用户信息告知Spring Security，后续我们就能调用security的接口获取到当前登录的用户信息。
 * 若前端发的请求不含JWT，我们也不能拦截该请求，因为一般的项目都是允许匿名访问的，有的接口允许不登录就能访问，没有JWT也放行是安全的，
 * 因为我们可以通过Spring Security进行权限管理，设置一些接口需要权限才能访问，不允许匿名访问


 JwtAuthenticationFilter继承了BasicAuthenticationFilter，该类用于普通http请求进行身份认证，
 该类有一个重要属性：AuthenticationManager，表示认证管理器，它是一个接口，它的默认实现类是ProviderManager，它与用户名密码认证息息相关，之后会详细解释。

 */

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private SysUserService sysUserService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(jwtUtils.getHeader());
        // 这里如果没有jwt，继续往后走，因为后面还有鉴权管理器等去判断是否拥有身份凭证，所以是可以放行的
        // 没有jwt相当于匿名访问，若有一些接口是需要权限的，则不能访问这些接口
        if (StrUtil.isBlankOrUndefined(jwt)) {
            chain.doFilter(request, response);
            return;
        }

        Claims claim = jwtUtils.getClaimsByToken(jwt);
        if (claim == null) {
            throw new JwtException("token 异常");
        }
        if (jwtUtils.isTokenExpired(claim)) {
            throw new JwtException("token 已过期");
        }

        String username = claim.getSubject();


        // 获取用户的权限等信息
        SysUser sysUser = sysUserService.getByUsername(username);


        // 构建UsernamePasswordAuthenticationToken,这里密码为null，是因为提供了正确的JWT,实现自动登录
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, userDetailService.getUserAuthority(sysUser.getId()));

        //若JWT验证成功，我们构建了一个UsernamePasswordAuthenticationToken对象，用于保存用户信息，之后将该对象交给SecurityContextHolder，set进它的context中，这样后续我们就能通过调用
        SecurityContextHolder.getContext().setAuthentication(token);
        //SecurityContextHolder.getContext().getAuthentication().getPrincipal()等方法获取到当前登录的用户信息了。

        chain.doFilter(request, response);

    }
}
