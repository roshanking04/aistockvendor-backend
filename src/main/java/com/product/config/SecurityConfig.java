/*
 * package com.product.config;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.security.config.annotation.web.builders.HttpSecurity;
 * import org.springframework.security.config.annotation.web.configuration.
 * EnableWebSecurity; import
 * org.springframework.security.config.http.SessionCreationPolicy; import
 * org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; import
 * org.springframework.security.crypto.password.PasswordEncoder; import
 * org.springframework.security.web.SecurityFilterChain; import
 * org.springframework.security.web.authentication.
 * UsernamePasswordAuthenticationFilter; import
 * org.springframework.web.cors.CorsConfiguration; import
 * org.springframework.web.cors.CorsConfigurationSource; import
 * org.springframework.web.cors.UrlBasedCorsConfigurationSource;
 * 
 * import java.util.Arrays; import java.util.List; import
 * org.springframework.http.HttpMethod;
 * 
 * 
 * @Configuration
 * 
 * @EnableWebSecurity public class SecurityConfig {
 * 
 * @Autowired private JwtFilter jwtFilter;
 * 
 * // @Bean // public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { // http // .csrf(csrf -> csrf.disable()) // .cors(cors ->
 * cors.configurationSource(corsConfigurationSource())) // .sessionManagement(s
 * -> // s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //
 * .authorizeHttpRequests(auth -> auth // // ✅ Allow ALL — no blocking, JWT
 * filter handles validation // .anyRequest().permitAll() // ) //
 * .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //
 * // return http.build(); // }
 * 
 * @Bean public SecurityFilterChain filterChain(HttpSecurity http) throws
 * Exception { http .csrf(csrf -> csrf.disable()) .cors(cors ->
 * cors.configurationSource(corsConfigurationSource())) .sessionManagement(s ->
 * s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 * .authorizeHttpRequests(auth -> auth
 * .requestMatchers("/api/auth/**").permitAll()
 * .requestMatchers("/uploads/**").permitAll() // images
 * .requestMatchers(HttpMethod.GET, "/product/all").permitAll() // public browse
 * .requestMatchers(HttpMethod.GET, "/product/search").permitAll()
 * .anyRequest().authenticated() ) .addFilterBefore(jwtFilter,
 * UsernamePasswordAuthenticationFilter.class);
 * 
 * return http.build(); }
 * 
 * @Bean public PasswordEncoder passwordEncoder() { return new
 * BCryptPasswordEncoder(); }
 * 
 * @Bean public CorsConfigurationSource corsConfigurationSource() {
 * CorsConfiguration config = new CorsConfiguration(); // ✅ Allow React on
 * localhost:3000 config.setAllowedOriginPatterns(List.of("http://localhost:*",
 * "http://127.0.0.1:*"));
 * config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","OPTIONS",
 * "PATCH")); config.setAllowedHeaders(List.of("*"));
 * config.setExposedHeaders(List.of("Authorization"));
 * config.setAllowCredentials(true); config.setMaxAge(3600L);
 * 
 * UrlBasedCorsConfigurationSource source = new
 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
 * config); return source; } }
 */
package com.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                // ✅ ALLOW EVERYTHING — no JWT check at all
                .anyRequest().permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
