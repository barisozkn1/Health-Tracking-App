package com.baris.healthtracking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF korumasını devre dışı bırak
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Yeni CORS yapılandırması
            .authorizeHttpRequests(auth -> auth
                // Açık endpointler
                .requestMatchers("/rest/api/auth/**").permitAll()  // Tüm 'auth' endpointleri
                .requestMatchers("/rest/api/psword/send-reset-token", "/rest/api/psword/reset-password").permitAll() // Şifre sıfırlama izinleri
                
                // Profil endpoint'lerini yalnızca giriş yapmış kullanıcılar erişebilir
                .requestMatchers("/rest/api/profile/**").authenticated()
                //
                .requestMatchers("rest/api/calorie/**").authenticated()
                .requestMatchers("rest/api/height-weight/**").authenticated()
                .requestMatchers("rest/api/heart-rate/**").authenticated()
                .requestMatchers("rest/api/sleep/**").authenticated()
                .requestMatchers("rest/api/step/**").authenticated()
                .requestMatchers("rest/api/water/**").authenticated()
                .requestMatchers("rest/api/test/**").authenticated()
                .anyRequest().authenticated() // Diğer tüm endpointler doğrulama ister
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Frontend URL
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // İzin verilen HTTP metotları
        corsConfiguration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "Accept")); // İzin verilen header'lar
        corsConfiguration.setAllowCredentials(true); // Cookie veya Token gönderimine izin ver
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration); // Tüm endpointler için geçerli
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Şifreleme için BCrypt kullan
    }
}
