package br.com.erudio.integrationtests.controllers.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerXMLTest  extends AbstractIntegrationTest{
	
	@LocalServerPort
    private int port;
	
	private static TokenDTO tokenDto;
    private static XmlMapper objectMapper;

	@BeforeAll
	static void setUp() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		tokenDto = new TokenDTO();
	}

	@Test
	@Order(1)
	void testSignin() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");
		
		var content = given()
				.basePath("/auth/signin")
				.port(port)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
				.body(credentials)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		tokenDto = objectMapper.readValue(content, TokenDTO.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}

	@Test
	@Order(2)
	void testRefreshToken() throws JsonMappingException, JsonProcessingException {
		var content = given()
				.basePath("/auth/refresh")
				.port(port)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
				.pathParam("username", tokenDto.getUsername())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
				.put("{username}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

				tokenDto = objectMapper.readValue(content, TokenDTO.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}

}
