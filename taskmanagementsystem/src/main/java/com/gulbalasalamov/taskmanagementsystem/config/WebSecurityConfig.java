package com.gulbalasalamov.taskmanagementsystem.config;

import com.gulbalasalamov.taskmanagementsystem.security.JwtTokenFilter;
import com.gulbalasalamov.taskmanagementsystem.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable); // prevent others accessing your tokens and make requests by using links
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // no session will be created or used by Spring security
        //Entry points
        http.authorizeRequests(auth -> auth
                .requestMatchers("/api/v1/user/**").permitAll()
                .requestMatchers("/api/v1/task/update/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/api/v1/task/all").hasRole("ADMIN")
                .requestMatchers("/api/v1/task/assign/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/task/filter/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/comment/**").hasAnyRole("ADMIN", "USER")
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
//                .requestMatchers("/v3/api-docs", "/configuration/ui", "/swagger-resources/**",
//                        "/configuration/security", "/swagger-ui.html", "/webjars/**","/index.html",
//                        "/swagger-ui/**", "/javainuse-openapi/**").permitAll()
                .anyRequest()
                .authenticated()); // authenticate means no role is important. login enough

        //Apply jwt token auth filter
        http.addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
