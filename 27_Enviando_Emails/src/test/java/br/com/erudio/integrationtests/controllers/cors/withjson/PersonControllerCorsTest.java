package br.com.erudio.integrationtests.controllers.cors.withjson;

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

import br.com.erudio.config.TestConfigs;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerCorsTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonDTO person;

	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonDTO();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/person/v1").setPort(port) // porta dinâmica
				.addFilter(new RequestLoggingFilter(LogDetail.ALL)).addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE).body(person).when().post().then().statusCode(200)
				.extract().body().asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		person = createdPerson;

		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

	}

	@Test
	@Order(2)
	void testCorsBlockedOrigin() throws JsonProcessingException {

		// Specification com origem não autorizada
		var blockedSpec = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:4000") // origem
																														// não
																														// permitida
				.setBasePath("/api/person/v1").setPort(port).addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		// Envio da requisição
		String content = given(blockedSpec).contentType(MediaType.APPLICATION_JSON_VALUE).body(person).when()
				.post().then()
				// Esperamos status 403 ou 401 dependendo da configuração do Spring
				.statusCode(403).extract().asString(); // 👈 aqui pega o conteúdo do body

		assertEquals("Invalid CORS request", content);

	}

	@Test
	@Order(3)
	void testFindById() throws JsonMappingException, JsonProcessingException {
		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_HOST3000)
				.setBasePath("/api/person/v1").setPort(port).addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		var content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getId()).when().get("{id}").then().statusCode(200).extract().body().asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
		person = createdPerson;

		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

		assertTrue(createdPerson.getId() > 0);
		assertTrue(createdPerson.getEnabled());
	}

	@Test
	@Order(4)
	void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
		specification = new RequestSpecBuilder().addHeader(TestConfigs.HEADER_PARAM_ORIGIN, "http://localhost:4000")
				.setBasePath("/api/person/v1").setPort(port).addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL)).build();

		String content = given(specification).contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getId()).when().get("{id}").then().statusCode(403).extract().body().asString();

		assertEquals("Invalid CORS request", content);
	}

	private void mockPerson() {
        person.setFirstName("Winston");
        person.setLastName("Churchill");
        person.setAddress("Oxfordshire - England");
        person.setGender("Male");
        person.setEnabled(true);
        person.setProfileUrl("https://pub.erudio.com.br/meus-cursos");
        person.setPhotoUrl("https://raw.githubusercontent.com/leandrocgsi/rest-with-spring-boot-and-java-erudio/main/photos/00_some_person.jpg");
    }
}
