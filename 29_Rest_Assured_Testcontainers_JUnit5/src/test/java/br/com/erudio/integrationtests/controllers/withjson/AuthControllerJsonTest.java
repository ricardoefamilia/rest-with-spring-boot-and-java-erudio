package br.com.erudio.integrationtests.controllers.withjson;

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

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest  extends AbstractIntegrationTest{
	
	@LocalServerPort
    private int port;
	
	private static TokenDTO tokenDto;

	@BeforeAll
	static void setUp() {

		tokenDto = new TokenDTO();
	}

	@Test
	@Order(1)
	void testSignin() {
		AccountCredentialsDTO credentials = new AccountCredentialsDTO("leandro", "admin123");
		
		tokenDto = given()
				.basePath("/auth/signin")
				.port(port)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(credentials)
				.when()
				.post()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}

	@Test
	@Order(2)
	void testRefreshToken() {
		tokenDto = given()
				.basePath("/auth/refresh")
				.port(port)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("username", tokenDto.getUsername())
				.header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getRefreshToken())
				.when()
				.put("{username}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(TokenDTO.class);
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}

}
