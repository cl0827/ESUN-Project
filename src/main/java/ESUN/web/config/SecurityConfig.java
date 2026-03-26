package ESUN.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // salt and hash
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            //允許跨域請求
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                corsConfiguration.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
                corsConfiguration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                corsConfiguration.setAllowedHeaders(java.util.List.of("*"));
                return corsConfiguration;
            }))
            // 關閉CSRF防護(前後端分離)
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                //把發文的API路徑加進允許通行
                .requestMatchers("/api/users/register", "/api/users/login", "/api/posts/**").permitAll()
                //其他沒有列出的路徑擋下來
                .anyRequest().authenticated()
            );
        return http.build();
    }
}