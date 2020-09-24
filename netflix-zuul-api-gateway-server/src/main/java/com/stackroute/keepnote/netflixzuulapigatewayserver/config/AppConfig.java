package com.stackroute.keepnote.netflixzuulapigatewayserver.config;

import com.stackroute.keepnote.netflixzuulapigatewayserver.filter.JWTValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean filterRegistrationBean(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new JWTValidationFilter());
        filterRegistrationBean.addUrlPatterns("/user-service/api/v1/*","/reminder-service/api/v1/*","/note-service/api/v1/*","/category-service/api/v1/*");
        return filterRegistrationBean;
    }

}
