package com.urbanNav.security.Configurations;

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // @Autowired
    // private SecurityInterceptor securityInterceptor;
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    // registry.addInterceptor(securityInterceptor)
    // .addPathPatterns("/**")
    // .excludePathPatterns("/public/**");
    // // Asegúrate de que las rutas sean las correctas

    // }

}
