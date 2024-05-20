package com.example.coolmate.Configrurations;

//import com.example.coolmate.Filters.JwtTokenFilter;

import com.example.coolmate.Filters.JwtTokenFilter;
import com.example.coolmate.Models.User.Role;
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

    // khi có requests gởi tớ sẽ chặn để kiểm tra, xem quyèn để đăng nhập
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(
                                    String.format("/%s/users/register", apiPrefix),
                                    String.format("/%s/users/login", apiPrefix)
                            )
                            .permitAll()
                            //request dc cấp quyền
                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/users/**", apiPrefix)).hasAnyRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/users/**", apiPrefix)).hasRole(Role.ADMIN)// vô hiệu hóa user

                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/orders", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/orders/delete/**", apiPrefix)).hasRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.POST,
                                    String.format("%s/order_details", apiPrefix)).hasRole(Role.USER)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/order_details/delete/**", apiPrefix)).hasAnyRole(Role.ADMIN)

                            .requestMatchers(HttpMethod.GET,
                                    String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN, Role.USER)
                            .requestMatchers(HttpMethod.DELETE,
                                    String.format("%s/categories/delete/**", apiPrefix)).hasRole(Role.ADMIN)
                            .requestMatchers(HttpMethod.PUT,
                                    String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                            .anyRequest().authenticated();
                });
        return http.build();
    }
}
