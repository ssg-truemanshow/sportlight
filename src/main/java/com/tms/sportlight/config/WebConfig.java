package com.tms.sportlight.config;

import com.tms.sportlight.controller.formatter.IdConverter;
import com.tms.sportlight.controller.formatter.LocalDateTimeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new IdConverter());
        registry.addFormatter(new LocalDateTimeFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:5173")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("Authorization", "Set-Cookie")
            .allowCredentials(true)
            .maxAge(3600);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
