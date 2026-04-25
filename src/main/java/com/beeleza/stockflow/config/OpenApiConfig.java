package com.beeleza.stockflow.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI stockflowOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("StockFlow API")
                        .description("API para gestão de estoque")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("StockFlow Team")
                                .email("dev@stockflow.com")))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}