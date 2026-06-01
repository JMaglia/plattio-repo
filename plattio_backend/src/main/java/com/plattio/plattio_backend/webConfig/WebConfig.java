package com.plattio.plattio_backend.webConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Permite CORS en todos los endpoints
                        .allowedOrigins("*") // Permite solicitudes desde cualquier origen (incluye Postman y navegadores)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
                        .allowedHeaders("*") // Headers permitidos
                        .allowCredentials(false); // Evita conflictos de credenciales
            }
        };
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Sirve imágenes desde src/main/resources/static/imagenes/
//        registry.addResourceHandler("/imagenes/**")
//                .addResourceLocations("file:src/main/resources/static/imagenes/");
//    }
}