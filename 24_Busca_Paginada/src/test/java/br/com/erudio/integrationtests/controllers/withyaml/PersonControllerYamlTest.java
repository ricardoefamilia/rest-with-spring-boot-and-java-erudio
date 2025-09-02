package br.com.erudio.integrationtests.controllers.withyaml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	private static PersonDTO person;

	@BeforeAll
	static void setUp() {
		objectMapper = new YAMLMapper();

		person = new PersonDTO();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(port) // porta dinâmica
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var createdPerson = given().config(
				RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
					.body(person, objectMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
					.body()
						.as(PersonDTO.class, objectMapper);

		person = createdPerson;

		assertNotNull(createdPerson.getId());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		
		assertTrue(createdPerson.getEnabled());

	}
	
	@Test
	@Order(2)
	void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Leonard Spencer-Churchill");
		
		var createdPerson = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
				.body(person, objectMapper)
				.when()
				.put()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
				.body()
				.as(PersonDTO.class, objectMapper);

		person = createdPerson;
		
		assertNotNull(createdPerson.getId());
		
		assertTrue(createdPerson.getId() > 0);
		
		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Leonard Spencer-Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		
		assertTrue(createdPerson.getEnabled());
		
	}
	
	@Test
	@Order(3)
	void testFindById() throws JsonMappingException, JsonProcessingException {

		var createdPerson = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
				.body()
				.as(PersonDTO.class, objectMapper);

		person = createdPerson;

		assertNotNull(createdPerson.getId());

		assertTrue(createdPerson.getId() > 0);

		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Leonard Spencer-Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());

		assertTrue(createdPerson.getEnabled());
	}
	
	@Test
	@Order(4)
	void testDisable() throws JsonMappingException, JsonProcessingException {
		
		var createdPerson = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.accept(MediaType.APPLICATION_YAML_VALUE)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
				.body()
				.as(PersonDTO.class, objectMapper);
		
		person = createdPerson;
		
		assertNotNull(createdPerson.getId());
		
		assertTrue(createdPerson.getId() > 0);
		
		assertEquals("Winston", createdPerson.getFirstName());
		assertEquals("Leonard Spencer-Churchill", createdPerson.getLastName());
		assertEquals("Oxfordshire - England", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		
		assertFalse(createdPerson.getEnabled());
	}
	
	@Test
	@Order(5)
	void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given(specification)
				.pathParam("id", person.getId())
				.when()
					.delete("{id}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(6)
	void testFindAll() throws JsonMappingException, JsonProcessingException {
	    // Cria algumas pessoas
	    for (int i = 1; i <= 5; i++) {
	        PersonDTO p = new PersonDTO();
	        p.setFirstName("Person" + i);
	        p.setLastName("Last" + i);
	        p.setAddress("Address" + i);
	        p.setGender("Male");
	        p.setEnabled(true);

	        given().config(
					RestAssuredConfig.config()
						.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
								MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
					)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
	            .body(p, objectMapper)
	        .when()
	            .post()
	        .then()
	            .statusCode(200)
	        .extract()
	            .body()
	            .as(PersonDTO.class, objectMapper);
	    }

	    // Agora sim chama o findAll
	    var response = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
	            .accept(MediaType.APPLICATION_YAML_VALUE)
	            .when()
	            .get()
	            .then()
	                .statusCode(200)
	            .extract()
	                .body()
	                .as(PersonDTO[].class, objectMapper);

	    List<PersonDTO> people = Arrays.asList(response);
	    assertFalse(people.isEmpty(), "A lista não deve estar vazia");
	}

	private void mockPerson() {
		person.setFirstName("Winston");
		person.setLastName("Churchill");
		person.setAddress("Oxfordshire - England");
		person.setGender("Male");
		person.setEnabled(true);
	}
}
