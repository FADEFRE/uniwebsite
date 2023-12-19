package swtp12.modulecrediting.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    
    private final UserAuthenticationEntryPoint userAuthenticationEntryPoint;

    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .exceptionHandling((exceptionHandling) -> exceptionHandling
                .authenticationEntryPoint(userAuthenticationEntryPoint))
            .addFilterBefore(new UsernamePasswordAuthFilter(), BasicAuthenticationFilter.class)
            .addFilterBefore(new CookieAuthenticationFilter(), UsernamePasswordAuthFilter.class)
            .csrf((csrf) -> csrf.disable())
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .logout((logout) -> logout.deleteCookies(CookieAuthenticationFilter.COOKIE_NAME))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(HttpMethod.POST, "/auth/signIn", "/auth/signUp")
                .permitAll()
            .anyRequest()
            .authenticated());

        return http.build();
    }
}
