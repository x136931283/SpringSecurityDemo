package com.tianblogs.security.handler;

import cn.hutool.json.JSONUtil;
import com.tianblogs.security.entity.Result;
import com.tianblogs.security.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录成功的处理器
 *
 * LoginSuccessHandler、LoginFailureHandler分别需要实现AuthenticationSuccessHandler和AuthenticationFailureHandler接口
 * ，需分别重写接口的onAuthenticationSuccess、onAuthenticationFailure方法，onAuthenticationSuccess方法的参数
 * 为HttpServletRequest、HttpServletResponse以及Authentication，onAuthenticationFailure方法的第三个参数与其不同，
 * 是AuthenticationException，表示登录失败对应的异常
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;

    /**
     *
     * @param authentication SpringSecurity中的接口Authentication继承了接口Principal，Principal接口表示主体的抽象概念，可用于表示任何实体，例如个人、公司和登录 ID，一般用来表示用户认证相关信息，调用其getName方法可以获得用户名
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        // 生成JWT，并放置到请求头中
        String jwt = jwtUtils.generateToken(authentication.getName());
        httpServletResponse.setHeader(jwtUtils.getHeader(), jwt);

        Result result = Result.succ(jwt);

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
