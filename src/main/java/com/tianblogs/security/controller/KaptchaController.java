package com.tianblogs.security.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import com.tianblogs.security.constant.Const;
import com.tianblogs.security.entity.Result;
import com.tianblogs.security.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 在前端渲染登录页面时，就会向后端请求获取验证码，该接口需将验证码图片用base64编码后传给前端，并将验证码对应的随机码也传给前端
 *
 * 验证码获取控制器
 * @author Administrator
 */
@RestController
public class KaptchaController {


    @Autowired
    private Producer producer;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);

        return Result.succ(
                MapUtil.builder()
                        .put("userKey", key)
                        .put("captcherImg", base64Img)
                        .build()
        );
    }

}
