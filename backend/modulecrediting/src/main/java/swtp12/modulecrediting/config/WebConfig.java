package swtp12.modulecrediting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.HttpMethod.DELETE;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    private static final long MAXAGESECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins( "http://localhost:5173","http://localhost:5174","http://localhost:4000", "http://localhost:4173", "http://172.26.92.91:8080")
                .allowedMethods(GET.name(), POST.name(), PUT.name(), DELETE.name())
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAXAGESECS);
    }
}
