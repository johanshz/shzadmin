package co.com.adminshz.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200"); // Permitir solicitudes desde este origen
        corsConfig.addAllowedHeader("*");
        corsConfig.addAllowedMethod("*");

        // Puedes agregar más configuraciones según tus necesidades

        return new CorsWebFilter(source -> corsConfig);
    }
}
