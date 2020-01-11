package com.udacity.course3.reviews.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * SwaggerConfig
 * <p>
 * Swagger configuration to test the following end points:
 * <p>
 * POST /products/
 * GET /products/
 * GET /products/{id}
 * POST comments/reviews/{reviewId}
 * GET comments/reviews/{reviewId}
 * POST reviews/products/{productId}
 * GET reviews/products/{productId}
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * Api docket.
     *
     * @return the docket
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }
}

