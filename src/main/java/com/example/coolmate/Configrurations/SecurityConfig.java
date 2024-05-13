package com.example.coolmate.Configrurations;


import com.example.coolmate.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        //phoneNumber phải là duy nhất
        return phoneNumber -> userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Cannot find user with phone number : " + phoneNumber));

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //mã hóa pass
        return new BCryptPasswordEncoder(); // đã thực thi mã hóa bằng BCrypt
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        //gọi user,pass để tạo auth
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            //xác thực thông tin người dùng
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
