package com.gulbalasalamov.taskmanagementsystem.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Task Management System")
                        .description("API Endpoint Decoration")
                        .version("v1.0")
                        .contact(newContact())
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("API Documentation")
                        .url("github.com/gulbalasalamov/task-management-system"));
    }

    private Contact newContact(){
        Contact contact = new Contact();
        contact.setName("Gulbala Salamov");
        contact.setUrl("https://www.github.com/gulbalasalamov");
        contact.setEmail("g.salamov@gmail.com");
        return contact;
    }
}
