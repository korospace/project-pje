package com.pje.kelompok4.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableWebMvc
public class SwaggerConfig {
    
    /**
     * Ui: http://localhost:9090/swagger-ui/index.html
     * Api: http://localhost:9090/v2/api-docs 
     */

    @Bean
    public Docket Api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .select()
            // .apis(RequestHandlerSelectors.any())
            .apis(RequestHandlerSelectors.basePackage("com.pje.kelompok4.controller"))
            .paths(PathSelectors.any())
            .build()
            .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
            "Project PJE Kel.4", // title
            "API Documentation for Project PJE Kel.4", // description 
            "v1.0.0", // version 
            "https://roadmap.sh/backend", // termsOfServiceUrl 
            null, // contact 
            "MIT", // license
            "https://roadmap.sh/backend", // licenseUrl 
            Collections.emptyList() // vendorExtensions
        );

        return apiInfo;
    }
}
