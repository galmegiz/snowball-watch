package com.fires.common.config
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * 스웨거 설정
 */
@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "FIRES API",
        description = "FIRES API",
        version = "1.0"
    )
)
class SwaggerConfig {
    /**
     * FIRES 전체 api
     */
    @Bean
    fun firesApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
            .group("FIRES api")
            .pathsToMatch("/api/v1/**")
            .addOpenApiCustomizer(buildSecurityOpenApi())
            .build()
    }

    /**
     * 인증관련
     */
    fun buildSecurityOpenApi(): OpenApiCustomizer? {
        // jwt token 설정 필요시 유지
        return OpenApiCustomizer { OpenApi: OpenAPI ->
            OpenApi.addSecurityItem(SecurityRequirement().addList("jwt token"))
                .components.addSecuritySchemes(
                    "jwt token",
                    SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .`in`(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer")
                )
        }
    }
}
