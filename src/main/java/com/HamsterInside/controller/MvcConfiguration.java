package com.HamsterInside.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    private Set<HandlerInterceptor> interceptorSet;

    public MvcConfiguration(Set<HandlerInterceptor> interceptorSet) {
        this.interceptorSet = interceptorSet;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        interceptorSet.stream().forEach(registry::addInterceptor);

    }
}
