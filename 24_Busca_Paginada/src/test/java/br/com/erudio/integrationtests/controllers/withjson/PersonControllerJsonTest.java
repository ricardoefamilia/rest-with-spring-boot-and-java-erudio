package br.com.erudio.integrationtests.controllers.withjson;

import static org.junit.jupiter.api.Assertions.*;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

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

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/person/v1")
				.setPort(port) // porta dinâmica
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
					.body(person)
				.when()
					.post()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(person)
				.when()
				.put()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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

		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getId())
				.when()
				.get("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();

		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		
		PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
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
	void testFindAll() throws JsonProcessingException {
	    // Cria algumas pessoas
	    for (int i = 1; i <= 5; i++) {
	        PersonDTO p = new PersonDTO();
	        p.setFirstName("Person" + i);
	        p.setLastName("Last" + i);
	        p.setAddress("Address" + i);
	        p.setGender("Male");
	        p.setEnabled(true);

	        var content = given(specification)
	            .contentType(MediaType.APPLICATION_JSON_VALUE)
	            .body(p)
	        .when()
	            .post()
	        .then()
	            .statusCode(200)
	        .extract()
	            .body()
	            .asString();

	        // Atualiza o objeto para validação se quiser
	        objectMapper.readValue(content, PersonDTO.class);
	    }

	    // Agora sim chama o findAll
	    var content = given(specification)
	            .accept(MediaType.APPLICATION_JSON_VALUE)
	            .when()
	            .get()
	            .then()
	                .statusCode(200)
	            .extract()
	                .body()
	                .asString();

	    List<PersonDTO> people = objectMapper.readValue(content, new TypeReference<List<PersonDTO>>() {});
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
