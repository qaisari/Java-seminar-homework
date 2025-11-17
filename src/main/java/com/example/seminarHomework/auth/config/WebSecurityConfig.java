package com.example.seminarHomework.auth.config;

import com.example.seminarHomework.auth.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig {
    @Autowired private CustomerUserDetailsService userDetailsService;
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.csrf(csrf -> csrf.disable())
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/saved",
                                            "/login", "/register", "/resources/**")
                        .permitAll()
                        .requestMatchers("/home").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
        ).formLogin(
                form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .defaultSuccessUrl("/home", true)
                        .permitAll()
        ).logout(
                logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
