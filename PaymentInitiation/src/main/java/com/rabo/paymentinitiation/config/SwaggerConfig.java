package com.rabo.paymentinitiation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.rabo.paymentinitiation")).paths(PathSelectors.any())
				.build().apiInfo(metaData()).useDefaultResponseMessages(false);
	}

	private ApiInfo metaData() {
		return new ApiInfoBuilder().title("Rabobank Payment Initiation Processor")
				.description("Payment initiation API for third party payment providers (TPPs).")
				.version("version 1.0.0")
				.build();
	}
}