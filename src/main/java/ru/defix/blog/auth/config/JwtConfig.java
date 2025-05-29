package ru.defix.blog.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import ru.defix.blog.auth.filter.JwtAccessAuthenticationFilter;
import ru.defix.blog.auth.filter.JwtRefreshAuthenticationFilter;
import ru.defix.blog.auth.service.UserDetailsServiceImpl;

@Configuration
public class JwtConfig {
    @Bean
    public JwtAccessAuthenticationFilter accessAuthenticationFilter(UserDetailsServiceImpl userDetailsService) throws Exception {
        return new JwtAccessAuthenticationFilter(userDetailsService);
    }

    @Bean
    public JwtRefreshAuthenticationFilter refreshAuthenticationFilter(UserDetailsServiceImpl userDetailsService,
                                                                      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        return new JwtRefreshAuthenticationFilter(userDetailsService, exceptionResolver);
    }
}
