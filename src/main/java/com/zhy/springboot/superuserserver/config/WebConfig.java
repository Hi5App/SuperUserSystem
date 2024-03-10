package com.zhy.springboot.superuserserver.config;

import com.zhy.springboot.superuserserver.interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author zhy
 * @Date 2024/2/26 13:50
 * @Description 在这个类中注册拦截器
 * @Since version-1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private MyInterceptor myInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor).addPathPatterns("/detect/*");
    }
}
