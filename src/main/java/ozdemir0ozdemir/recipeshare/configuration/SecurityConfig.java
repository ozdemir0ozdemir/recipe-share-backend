package ozdemir0ozdemir.recipeshare.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import ozdemir0ozdemir.recipeshare.filter.JwtTokenFilter;
import ozdemir0ozdemir.recipeshare.service.JwtService;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApplicationProperties properties;
    private final JwtService jwtService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration configuration) throws Exception {

        // Disable CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        // Stateless Session
        http.sessionManagement(manage -> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Authorize Requests
        http.authorizeHttpRequests(this.authorizeHttpRequests());

        // Cors Configuration
        http.cors(cors -> cors.configurationSource(this.corsConfigurationSource()));

        // Activate Basic Authentication
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Activate Form Login
        http.formLogin(AbstractHttpConfigurer::disable);

        // Filter
        http.addFilterBefore(new JwtTokenFilter(jwtService, configuration.getAuthenticationManager()), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            var configuration = new CorsConfiguration();
            configuration.addAllowedOrigin("http://localhost:4200");
            configuration.setMaxAge(Duration.ofHours(1L));
            return configuration;
        };
    }
    private Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry>
    authorizeHttpRequests() {
        return request -> {
            for(String pattern: properties.getAuthenticatedServletPaths()){
                request.requestMatchers(pattern).authenticated();
            }

            request.anyRequest().permitAll();
        };
    }


}
