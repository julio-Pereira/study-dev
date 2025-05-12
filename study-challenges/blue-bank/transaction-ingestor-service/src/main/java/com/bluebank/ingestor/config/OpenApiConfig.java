package com.bluebank.ingestor.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name")
    private String applicationName;


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(
                new Info()
                    .title("Transaction Ingestor API")
                    .description("API for ingesting financial transactions")
                    .version("25.1.1")
                    .contact(
                        new Contact()
                        .name("Bluebank Team")
                        .email("dev@bluebank.com")
                        .url("https://bluebank.com/contact"))
                    .license(
                        new License()
                        .name("Proprietary")
                        .url("https://bluebank.com/license")))
            .servers(
                    List.of(
                            new Server().url("/").description("Default Server")
                    ))
            .components(new Components());
    }

}
