package com.tianblogs.security.filter;

import com.tianblogs.security.utils.CommonUtil;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class CustomAuthenticationFilter  extends UsernamePasswordAuthenticationFilter  {

    private static final String APPLICATION_JSON_UTF8_VALUE_WITH_SPACE = "application/json; charset=UTF-8";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 通过 ContentType 判断是否是 JSON 登录
        String contentType = request.getContentType();
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType) ||
                APPLICATION_JSON_UTF8_VALUE_WITH_SPACE.equalsIgnoreCase(contentType)) {

            // 获取 body 参数 Map
            Map<String, Object> obj = CommonUtil.getBodyParametersFromRequest(request);
            // 使用用户名密码构建 Token
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    obj.get(USERNAME), obj.get(PASSWORD));
            setDetails(request, token);

            return this.getAuthenticationManager().authenticate(token);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }
}
