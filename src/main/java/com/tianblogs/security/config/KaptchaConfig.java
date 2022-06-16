package com.tianblogs.security.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码生成使用的是谷歌的验证码工具类，配置类如下：
 * @author Administrator
 */
@Configuration
public class KaptchaConfig {

    @Bean
    DefaultKaptcha producer() {
        Properties properties = new Properties();
        properties.put("kaptcha.border", "no");
        properties.put("kaptcha.textproducer.font.color", "black");
        properties.put("kaptcha.textproducer.char.space", "4");
        properties.put("kaptcha.image.height", "40");
        properties.put("kaptcha.image.width", "120");
        properties.put("kaptcha.textproducer.font.size", "30");
        Config config = new Config(properties);
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();//DefaultKaptcha实现了Producer接口，Producer接口用于生成验证码，调用其createText()方法即可生成字符串验证码
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

}
