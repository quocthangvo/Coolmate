package com.example.coolmate.Configrurations;

import com.example.coolmate.Filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("%s/users/register", apiPrefix),
                                    String.format("%s/users/login", apiPrefix)
                            ).permitAll()
                            .requestMatchers(HttpMethod.GET).permitAll()// Cho phép tất cả các request GET
                            .requestMatchers(HttpMethod.POST).permitAll()
                            .requestMatchers(HttpMethod.DELETE).permitAll()
                            .requestMatchers(HttpMethod.PUT).permitAll() //khi phân quyền r bỏ này
//                            .requestMatchers(HttpMethod.PUT,
//                                    String.format("%s/product_details/**", apiPrefix)).hasRole(Role.ADMIN)
                            .anyRequest().authenticated(); // yêu cầu các request khác có quyền

                });
        return http.build();
    }
}
