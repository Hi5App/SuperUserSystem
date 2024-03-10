package com.zhy.springboot.superuserserver.config;
import com.zhy.springboot.superuserserver.filter.MyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author zhy
 * @Date 2024/2/26 19:29
 * @Description This is description of class
 * @Since version-1.0
 */
@Configuration
public class MyFilterConfig {
    @Bean(name="myFilter")
    public MyFilter myFilter(){
        return new MyFilter();
    }

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> myFilterBean() {
        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<>();
        registration.setFilter(new DelegatingFilterProxy("myFilter"));
        registration.setUrlPatterns(Collections.singletonList("/detect/missing"));
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setEnabled(false);
        return registration;
    }
}
