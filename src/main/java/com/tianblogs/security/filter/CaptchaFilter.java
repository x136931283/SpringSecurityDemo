package com.tianblogs.security.filter;

import com.tianblogs.security.constant.Const;
import com.tianblogs.security.handler.LoginFailureHandler;
import com.tianblogs.security.handler.error.CaptchaException;
import com.tianblogs.security.utils.CommonUtil;
import com.tianblogs.security.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 验证码拦截器
 *
 * 在验证码过滤器中，需要先判断请求是否是登录请求，若是登录请求，则进行验证码校验，从redis中通过userKey查找对应的验证码，
 * 看是否与前端所传验证码参数一致，当校验成功时，因为验证码是一次性使用的，一个验证码对应一个用户的一次登录过程，
 * 所以需用hdel将存储的HASH删除。当校验失败时，则交给登录认证失败处理器LoginFailureHandler进行处理
 *
 *
 * 请求发向 servlet 时会被 Filter 拦截，如果 servlet 将请求转发给另一个 servlet，请求发向第二个 servlet 时，依旧会被相同的 Filter 拦截。结果就是一个请求被同一个 Filter 拦截了两次。
 * `OncePerRequestFilter` 一个请求只被过滤器拦截一次。请求转发不会第二次触发过滤器。
 *
 * 它能够确保在一次请求中只通过一次filter，而不会重复执行
 *
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 登录失败处理器
     */
    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        String url = httpServletRequest.getRequestURI();
        if ("/login".equals(url) && "POST".equals(httpServletRequest.getMethod())) {
            // 校验验证码
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    // 校验验证码逻辑
    private void validate(HttpServletRequest httpServletRequest) {
        Map<String, Object> bodyParametersFromRequest = CommonUtil.getBodyParametersFromRequest(httpServletRequest);

        String code = (String) bodyParametersFromRequest.get("code");
        String key = (String) bodyParametersFromRequest.get("userKey");

        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }

        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, key))) {
            throw new CaptchaException("验证码错误");
        }

        // 若验证码正确，执行以下语句
        // 一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY, key);
    }
}
