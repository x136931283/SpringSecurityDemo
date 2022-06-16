package com.tianblogs.security.handler.password;

import com.tianblogs.security.constant.RsaProperties;
import com.tianblogs.security.utils.RSAUtils;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Spring Security所需的组件我们基本上都写得差不多了，还剩一个，那就是密码的加密解密，首先，在我们的数据库中，我们不可能存明文密码，这样非常不安全，
 * 因此Spring Security在拿到密码时，需要对密码进行解密加密过程，再和数据库中的密文进行比较。Spring Security拿到的密码也是密文，因为在前端的登录请求中，
 * 不可能将password字段设置成明文进行网络传输，这比数据库存明文更危险，因此，传到后端的password字段也是加密过的，于是，Spring Security需要先按前端的加密规则解密，
 * 再根据数据库的加密规则加密，最后和数据库中的密文进行比较
 *
 *   在这里我们设前端使用rsa对密码进行加密，后端使用BCrypt对密码进行加密
 *   SpringSecurity提供了用于密码加密解密的工具类BCryptPasswordEncoder，
 * 不过我们需自定义PasswordEncoder类，并使其继承BCryptPasswordEncoder，
 * 因为security提供的该类并没有考虑前端加密的问题。我们需要重写其matches方法，
 * 该方法用于判断从前端接收的密码与数据库中的密码是否一致

 */
@NoArgsConstructor
public class PasswordEncoder extends BCryptPasswordEncoder {

    //private String pwds = "";

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 接收到的前端的密码
        String pwd = rawPassword.toString();
        // 进行rsa解密
        try {
            pwd = RSAUtils.decryptByPrivateKey(RsaProperties.privateKey, pwd);
            //pwds = pwd;
            //String encode = encode(pwds);
            //System.out.println(encode);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
        if (encodedPassword != null && encodedPassword.length() != 0) {
            return BCrypt.checkpw(pwd, encodedPassword);
        } else {
            return false;
        }
    }

    /**
     * 注册的时候使用此方法对密码加密
     * @param rawPassword
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }
}
