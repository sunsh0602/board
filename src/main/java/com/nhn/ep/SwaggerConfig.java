package com.nhn.ep;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/project/v1/cloud/**")
                .build();
    }
    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("EP클라우드운영팀 게시판 API")
                        .description("클라우드운영팀 스터디 프로젝트 API 명세서입니다.")
                        .version("v0.0.1"));
    }
}
