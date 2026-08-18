package com.fooddelivery.foodbackend.config;

import com.fooddelivery.foodbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DaoAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // ── Public: Auth endpoints ─────────────────────────────
                        .requestMatchers("/api/auth/**").permitAll()

                        // ── Public: Browse restaurants & menu (read-only) ──────
                        .requestMatchers(HttpMethod.GET,
                                "/api/restaurants/**",
                                "/api/categories/**",
                                "/api/menu-items/**"
                        ).permitAll()

                        // ── Customer endpoints ────────────────────────────────
                        .requestMatchers("/api/cart/**").hasRole("USER")
                        .requestMatchers("/api/cart/items/**").hasRole("USER")
                        .requestMatchers("/api/group-carts/**").hasRole("USER")
                        .requestMatchers("/api/orders/**").hasRole("USER")
                        .requestMatchers("/api/payments/**").hasRole("USER")
                        .requestMatchers("/api/addresses/**").hasRole("USER")    // fixed: was /api/address/**

                        // ── Restaurant Owner endpoints ────────────────────────
                        .requestMatchers("/api/restaurants/**").hasRole("RESTAURANT_OWNER") // fixed: was RESTAURANT
                        .requestMatchers(HttpMethod.POST, "/api/restaurants").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.PUT, "/api/restaurants/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasRole("RESTAURANT_OWNER")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasAnyRole("USER", "RESTAURANT_OWNER")

                        // ── Admin endpoints ────────────────────────────────────
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}