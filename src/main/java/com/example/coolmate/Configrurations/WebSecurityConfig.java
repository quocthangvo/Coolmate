package com.example.coolmate.Configrurations;

//import com.example.coolmate.Filters.JwtTokenFilter;

import com.example.coolmate.Filters.JwtTokenFilter;
import com.example.coolmate.Models.Role;
import lombok.RequiredArgsConstructor;
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

    // khi có requests gởi tớ sẽ chặn để kiểm tra, xem quyèn để đăng nhập
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("/%s/users/register", "api/v1"),
                                    String.format("/%s/users/login", "api/v1")
                            )
                            .permitAll()
//                            .requestMatchers(HttpMethod.PUT, String.format("%s/orders", "api/v1")).hasRole("ADMIN")
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/users?**", "api/v1")).hasRole(Role.ADMIN)
                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
