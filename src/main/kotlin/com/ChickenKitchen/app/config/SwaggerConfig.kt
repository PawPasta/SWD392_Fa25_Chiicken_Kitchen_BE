package com.ChickenKitchen.app.config

import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .openapi("3.1.0")
            .info(
                Info()
                    .title("Demo API")
                    .description("API cho thử nghiệm project")
                    .version("1.0")
                    .license(License().name("Apache 2.0").url("http://springdoc.org"))
            )
            .externalDocs(
                ExternalDocumentation()
                    .description("Xem thêm tại SpringDoc")
                    .url("https://springdoc.org")
            )
            .components(
                Components().addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Nhập JWT token để xác thực")
                )
            )
            .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
            // Chạy local thì phong ấn cái này lại.
            .servers(
                listOf(
                    Server().url("https://chickenkitchen.milize-lena.space")
                        .description("Production server")
                )
            )
    }
}
