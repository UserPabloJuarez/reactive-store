package com.interbank.reactive_store.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Reactive API")
                        .version("1.0.0")
                        .description("API Reactiva para Gestión de E-Commerce con Spring WebFlux")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("desarrollo@ecommerce.com")));
    }
}
