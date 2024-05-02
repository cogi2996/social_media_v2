package com.example.social_media.config;

import com.example.social_media.filter.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static com.example.social_media.security.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final RateLimitFilter rateLimitFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.contentSecurityPolicy(csp -> csp.policyDirectives(
                        "script-src 'self' https://unpkg.com/axios@1.6.7/dist/axios.min.js https://www.gstatic.com/firebasejs/10.9.0/firebase-storage.js https://cdn.jsdelivr.net/npm/js-cookie@3.0.5/dist/js.cookie.min.js https://www.gstatic.com/firebasejs/10.9.0/firebase-app.js https://www.gstatic.com/firebasejs/10.9.0/firebase-database.js https://*.firebaseio.com;" +
                                "form-action 'self';" +
                                "frame-ancestors 'self';" +
                                "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com/css;" +
                                "img-src 'self' https://firebasestorage.googleapis.com data:;" +
                                "default-src 'self' https://fonts.gstatic.com/s/poppins/v21/*;" +
                                "base-uri 'self';" +
                                "connect-src 'self' wss://*.firebaseio.com;" + // Updated this line
                                "frame-src 'self' https://*.firebaseio.com;" + // Added this line
                                "font-src 'self' https://fonts.gstatic.com data:;"
                )))
                .authorizeHttpRequests
                        (
                                req ->
                                        req.requestMatchers(
                                                        "/auth/**",
                                                        "/assets/**","/**")
                                                .permitAll()
                                                .requestMatchers("/admin/**").hasAnyRole(ADMIN.name())
                                                .anyRequest()
                                                .authenticated()
                        )
                .addFilterBefore(rateLimitFilter, ChannelProcessingFilter.class) // Add the filter before the ChannelProcessingFilter
                .formLogin(
                        login -> login
                                .loginPage("/auth/login")
                                .permitAll()
                                .defaultSuccessUrl("/home/index", true)
                                .failureUrl("/auth/login?error=true")
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
