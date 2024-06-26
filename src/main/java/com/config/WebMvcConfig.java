package com.config;

import com.filter.EmpFilter;
import com.filter.MemFilter;
import com.filter.PermissionFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new EmpFilter()).order(1);
        registry.addInterceptor(new EmpFilter()).order(1);
        registry.addInterceptor(new PermissionFilter()).order(2);
        registry.addInterceptor(new MemFilter()).order(3);
    }
}