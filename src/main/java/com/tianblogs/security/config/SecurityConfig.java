package com.tianblogs.security.config;

import com.tianblogs.security.filter.CaptchaFilter;
import com.tianblogs.security.filter.CustomAuthenticationFilter;
import com.tianblogs.security.filter.JwtAuthenticationFilter;
import com.tianblogs.security.handler.LoginFailureHandler;
import com.tianblogs.security.handler.LoginSuccessHandler;
import com.tianblogs.security.handler.jwt.JWTLogoutSuccessHandler;
import com.tianblogs.security.handler.jwt.JwtAccessDeniedHandler;
import com.tianblogs.security.handler.jwt.JwtAuthenticationEntryPoint;
import com.tianblogs.security.handler.password.PasswordEncoder;
import com.tianblogs.security.user.UserDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 整合所有组件，进行Spring Security全局配置：SecurityConfig
 *   到此为止，需要我们自己实现的过滤器和处理器等就都准备好了，接下来只需要进行Spring Security的全局配置了，我们定义SecurityConfig作为其配置，
 * 该类需要继承WebSecurityConfigurerAdapter(采用适配器模式，继承后SecurityConfig可以看做是WebSecurityConfigurer），
 * SecurityConfig需要使用@EnableGlobalMethodSecurity(prePostEnabled = true)注解。
 *   Spring Security默认是禁用注解的，要想开启注解，需要在继承WebSecurityConfigurerAdapter的类上加@EnableGlobalMethodSecurity注解，
 * 来判断用户对某个控制层的方法是否具有访问权限。prePostEnabled = true即可在方法前后进行权限检查
 *
 *   Security内置的权限注解如下：
 *   @PreAuthorize：方法执行前进行权限检查
 *   @PostAuthorize：方法执行后进行权限检查
 *   @Secured：类似于 @PreAuthorize
 *   可以在Controller的方法前添加这些注解表示接口需要什么权限。
 *
 *
 *
 *   配置类还需使用@EnableWebSecurity注解，该注解有两个作用：
 *          1. 加载了WebSecurityConfiguration配置类, 配置安全认证策略。
 *          2.加载了AuthenticationConfiguration, 配置了认证信息。AuthenticationConfiguration这个类的作用就是用来创建ProviderManager。
 *
 *
 *   @EnableWebSecurity完成的工作便是加载了WebSecurityConfiguration，AuthenticationConfiguration这两个核心配置类，
 *    也就此将spring security的职责划分为了配置安全信息，配置认证信息两部分。
 *
 *
 *   在SecurityConfig这个配置类中，我们需要将之前写的拦截器和处理器都autowire进来，并使用@Bean注解，
 * 声明JwtAuthenticationFilter和PasswordEncoder的构造函数。在JwtAuthenticationFilter的构造函数中，我们调用authenticationManager()
 * 方法给JwtAuthenticationFilter提供AuthenticationManager。
 *   配置类需要重写configure方法进行配置，该方法有多种重载形式，我们使用其中的两种，其中一个用于配置url安全拦截配置，
 * 另一个用于AuthenticationManager配置UserDetailsService的实现类
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) //可在方法前后以注解的方式进行权限检查
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Autowired
    LoginSuccessHandler loginSuccessHandler;

    @Autowired
    CaptchaFilter captchaFilter;

    @Autowired
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    UserDetailServiceImpl userDetailService;

    @Autowired
    JWTLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }


    private static final String[] URL_WHITELIST = {
      "/login",         //登录
      "/logout",        //登出
      "/captcha",        //验证码
      "/user/register", //注册
      "/favicon.ico"
    };

    /**
     * 使用 Spring Security 自带的密码加密器
     */
    @Bean
    PasswordEncoder PasswordEncoder() {
        return new PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()

                // 登录配置
                .formLogin()
                //.successHandler(loginSuccessHandler) 使用自定义配置登录拦截器，这里无需配置
                //.failureHandler(loginFailureHandler) 使用自定义配置登录拦截器，这里无需配置

                .and()
                .logout()
                .logoutSuccessHandler(jwtLogoutSuccessHandler)

                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
                // 异常处理器
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())

                //自定义登录拦截 用customAuthenticationFilter 替换 UsernamePasswordAuthenticationFilter
                .addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                // 验证码过滤器放在UsernamePassword过滤器之前
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
    }

    /**
     * 登录配置，使用自定义的用户名密码验证过滤器
     */
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(loginSuccessHandler);
        filter.setAuthenticationFailureHandler(loginFailureHandler);
        // 可自定义登录接口请求路径
        // filter.setFilterProcessesUrl("");
        filter.setAuthenticationManager(authenticationManagerBean());

        return filter;
    }
}
