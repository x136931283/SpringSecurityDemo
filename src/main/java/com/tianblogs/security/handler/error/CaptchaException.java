package com.tianblogs.security.handler.error;

import org.springframework.security.core.AuthenticationException;

/**
 * 这里我们自定义了验证码错误的异常，它继承了Spring Security的AuthenticationException：
 */
public class CaptchaException extends AuthenticationException {


    public CaptchaException(String msg) {
        super(msg);
    }
}
