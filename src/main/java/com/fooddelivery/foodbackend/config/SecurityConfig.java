package com.fooddelivery.foodbackend.config; ////package com.fooddelivery.foodbackend.config;
////
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.http.HttpMethod;
////import org.springframework.security.config.Customizer;
////import org.springframework.security.config.annotation.web.builders.HttpSecurity;
////import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.web.SecurityFilterChain;
////
////@Configuration
////public class SecurityConfig {
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http.csrf(csrf -> csrf.disable())
////                .authorizeHttpRequests(auth -> auth.requestMatchers(
////                        "/api/auth/**"
////                ).permitAll()
////                        .requestMatchers(HttpMethod.GET,
////                                "/api/restaurants/**")
////                .permitAll()
////                        .anyRequest().authenticated()
////                ).sessionManagement(session -> session.sessionCreationPolicy
////                        (SessionCreationPolicy.STATELESS)
////                )
////                .httpBasic(Customizer.withDefaults());
////
////        return http.build();
////    }
////}
//
//package com.fooddelivery.foodbackend.config;
//
import com.fooddelivery.foodbackend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

import com.fooddelivery.foodbackend.security.CustomUserDetailsService;

//@Configuration
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    private final CustomUserDetailsService customUserDetailsService;
//
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http)
//            throws Exception {
//
//        http
//
//                .csrf(csrf -> csrf.disable())
//
//                .authorizeHttpRequests(auth -> auth
//

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

////                        .requestMatchers("/api/auth/**").permitAll()
//                                .requestMatchers(
//                                        "/api/auth/**",
//                                        "/v3/api-docs/**",
//                                        "/swagger-ui/**",
//                                        "/swagger-ui.html"
//                                ).permitAll()
//
//                        .anyRequest().authenticated()
//
//                )
//
//                .sessionManagement(session ->
//
//                        session.sessionCreationPolicy(
//                                SessionCreationPolicy.STATELESS
//                        )
//                )
//
//                .authenticationProvider(authenticationProvider())
//
//                .addFilterBefore(
//                        jwtAuthenticationFilter,
//                        UsernamePasswordAuthenticationFilter.class
//                );
//
//        return http.build();
//
//    }
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//
//        DaoAuthenticationProvider provider =
//                new DaoAuthenticationProvider();
//        provider.setUserDetailsService(customUserDetailsService);
//        provider.setPasswordEncoder(passwordEncoder);
//        return provider;
//
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration config)
//            throws Exception {
//
//        return config.getAuthenticationManager();
//
//    }
//
//}
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DaoAuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Public APIs
                        .requestMatchers(
                                "/api/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Public Read APIs
                        .requestMatchers(HttpMethod.GET,
                                "/api/restaurants/**",
                                "/api/categories/**",
                                "/api/menu-items/**"
                        ).permitAll()

                        // Customer APIs
                        .requestMatchers("/api/cart/**")
                        .hasRole("USER")

                        .requestMatchers("/api/orders/**")
                        .hasRole("USER")

                        .requestMatchers("/api/payments/**")
                        .hasRole("USER")

                        .requestMatchers("/api/address/**")
                        .hasRole("USER")

                        // Restaurant APIs
                        .requestMatchers("/api/restaurant/**")
                        .hasRole("RESTAURANT")

                        // Admin APIs
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()

                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}