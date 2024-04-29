package com.example.social_media.config;

import com.example.social_media.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static com.example.social_media.security.Permission.*;
import static com.example.social_media.security.Role.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests
                        (
                                req ->
                                        req.requestMatchers(
                                                        "/auth/**",
                                                        "/assets/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAnyRole(ADMIN.name())
                                                .anyRequest()
                                                .authenticated()
                        )

                .formLogin(
                        login -> login
                                .loginPage("/auth/login")
                                .permitAll()
//                                .loginProcessingUrl("/auth/login")
                                .defaultSuccessUrl("/home/index", true)
                                .failureUrl("/auth/login?error=true")
                                // username and password parameter names
//                                .usernameParameter("email")
//                                .passwordParameter("password")
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authenticationProvider(authenticationProvider)
                .logout
                        (
                                logout -> logout
                                        .logoutUrl("/auth/logout")
                                        .addLogoutHandler(logoutHandler)
                                        .logoutSuccessHandler(logoutSuccessHandler)
                        )

        ;
        return http.build();
    }

}
