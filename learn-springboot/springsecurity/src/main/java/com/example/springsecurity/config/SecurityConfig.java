package com.example.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // /**
    //  * 自定义用户认证逻辑
    //  */
    // @Autowired
    // private UserDetailsService userDetailsService;
    //
    // /**
    //  * 身份认证接口
    //  */
    // @Override
    // protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     // 使用自定义用户信息读取类
    //     auth.userDetailsService(userDetailsService) // <X>
    //             // 进行密码加密
    //             .passwordEncoder(bCryptPasswordEncoder()); // <Y>
    // }
    //
    // /**
    //  * 强散列哈希加密实现
    //  */
    // @Bean
    // public BCryptPasswordEncoder bCryptPasswordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
    //
    // /**
    //  * 认证失败处理类
    //  */
    // @Autowired
    // private AuthenticationEntryPointImpl unauthorizedHandler;
    //
    // /**
    //  * 退出处理类
    //  */
    // @Autowired
    // private LogoutSuccessHandlerImpl logoutSuccessHandler;
    //
    // /**
    //  * token 认证过滤器
    //  */
    // @Autowired
    // private JwtAuthenticationTokenFilter authenticationTokenFilter;
    //
    // @Override
    // protected void configure(HttpSecurity httpSecurity) throws Exception {
    //     httpSecurity
    //             // CRSF禁用，因为不使用session
    //             .csrf().disable()
    //             // <X> 认证失败处理类
    //             .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
    //             // 基于token，所以不需要session
    //             .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
    //             // 过滤请求
    //             .authorizeRequests()
    //             // <Y> 对于登录login 验证码captchaImage 允许匿名访问
    //             .antMatchers("/login", "/captchaImage").anonymous()
    //             .antMatchers(
    //                     HttpMethod.GET,
    //                     "/*.html",
    //                     "/**/*.html",
    //                     "/**/*.css",
    //                     "/**/*.js"
    //             ).permitAll()
    //             .antMatchers("/profile/**").anonymous()
    //             .antMatchers("/common/download**").anonymous()
    //             .antMatchers("/swagger-ui.html").anonymous()
    //             .antMatchers("/swagger-resources/**").anonymous()
    //             .antMatchers("/webjars/**").anonymous()
    //             .antMatchers("/*/api-docs").anonymous()
    //             .antMatchers("/druid/**").anonymous()
    //             // 除上面外的所有请求全部需要鉴权认证
    //             .anyRequest().authenticated()
    //             .and()
    //             .headers().frameOptions().disable();
    //     httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler); // <Z>
    //     // <P> 添加 JWT filter
    //     httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    // }
    //
    // @Bean
    // @Override
    // public AuthenticationManager authenticationManagerBean() throws Exception {
    //     return super.authenticationManagerBean();
    // }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                // <X> 使用内存中的 InMemoryUserDetailsManager
                        inMemoryAuthentication()
                // <Y> 不使用 PasswordEncoder 密码编码器
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                // <Z> 配置 admin 用户
                .withUser("admin").password("admin").roles("ADMIN")
                // <Z> 配置 normal 用户
                .and().withUser("normal").password("normal").roles("NORMAL");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // <X> 配置请求地址的权限
                .authorizeRequests()
                .antMatchers("/test/echo").permitAll() // 所有用户可访问
                .antMatchers("/test/admin").hasRole("ADMIN") // 需要 ADMIN 角色
                // .antMatchers("/test/normal").access("hasRole('ROLE_NORMAL')") // 需要 NORMAL 角色。
                // 任何请求，访问的用户都需要经过认证
                .anyRequest().authenticated()
                .and()
                // <Y> 设置 Form 表单登录
                .formLogin()
//                    .loginPage("/login") // 登录 URL 地址
                .permitAll() // 所有用户可访问
                .and()
                // 配置退出相关
                .logout()
//                    .logoutUrl("/logout") // 退出 URL 地址
                .permitAll(); // 所有用户可访问
    }
}
