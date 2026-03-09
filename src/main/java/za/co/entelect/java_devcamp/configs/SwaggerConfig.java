package za.co.entelect.java_devcamp.configs;

import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    final String bearerScheme = "bearerAuth";
    final String apiKeyScheme = "apiKeyAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product Shop API Documentation")
                        .version("1.0.0")
                        .description("This document provides API details for the Product Shop Spring Boot Project"))
                .addSecurityItem(new SecurityRequirement()
                        .addList(bearerScheme)
                        .addList(apiKeyScheme))
                .components(new Components()
                        .addSecuritySchemes(bearerScheme,
                                new SecurityScheme()
                                        .name(bearerScheme)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                        .addSecuritySchemes(apiKeyScheme,
                                new SecurityScheme()
                                        .name("X-API-KEY")
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER))
                );
    }

}
