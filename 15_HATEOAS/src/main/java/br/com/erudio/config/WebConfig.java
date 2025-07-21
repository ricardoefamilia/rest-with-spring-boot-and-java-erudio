package br.com.erudio.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		
		
		// Via Extensão .xml e .json Deprecated on Spring Boot 2.6
		// exemplo descontinuado: localhost:8080/api/person/v1/2.json
		// VIA QUERY PARAM: http://localhost:8080/api/person/v1/2?mediaType=xml or http://localhost:8080/api/person/v1/2?mediaType=json
		/**
		configurer.favorParameter(true)
		.parameterName("mediaType")
		.ignoreAcceptHeader(true)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
			.mediaType("json", MediaType.APPLICATION_JSON)
			.mediaType("xml", MediaType.APPLICATION_XML);
		*/
		//VIA HEADER PARAM: http://localhost:8080/api/person/v1/2?mediaType=xml or http://localhost:8080/api/person/v1/2?mediaType=json
		configurer.favorParameter(false)
		.ignoreAcceptHeader(false)
		.useRegisteredExtensionsOnly(false)
		.defaultContentType(MediaType.APPLICATION_JSON)
		.mediaType("json", MediaType.APPLICATION_JSON)
		.mediaType("xml", MediaType.APPLICATION_XML)
		.mediaType("yaml", MediaType.APPLICATION_YAML);
		
	}

	
}
