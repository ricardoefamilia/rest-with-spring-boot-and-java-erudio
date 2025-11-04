package br.com.erudio.integrationtests.controllers.withjson;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import br.com.erudio.integrationtests.response.json.EmbeddedResponsePersonJson;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static PersonDTO person;
	private static TokenDTO tokenDto;

	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		person = new PersonDTO();
		tokenDto = new TokenDTO();
	}
	
	@Test
	@Order(0)
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
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
				.setBasePath("/api/person/v1")
				.setPort(port) // porta dinâmica
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		assertNotNull(tokenDto.getAccessToken());
		assertNotNull(tokenDto.getRefreshToken());
	}

	@Test
	@Order(1)
	void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

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
	@DisplayName("Deve retornar a página 3 e validar posições específicas")
	void testFindAllPeopleWithPagination() throws JsonProcessingException {

	    // Chamada GET no endpoint com paginação
	    String content = given(specification)
	            .accept(MediaType.APPLICATION_JSON_VALUE)
	            .queryParams("page", 3, "size", 12, "direction", "asc")
	        .when()
	            .get()
	        .then()
	            .statusCode(200)
	        .extract()
	            .body()
	            .asString();

	    // Converte o JSON para EmbeddedResponse<PersonDTO>
	    EmbeddedResponsePersonJson<PersonDTO> response = objectMapper.readValue(
	            content, new TypeReference<EmbeddedResponsePersonJson<PersonDTO>>() {});

	    // Extrai a lista de pessoas dentro de "_embedded"
	    List<PersonDTO> people = response.getEmbedded().getPeople();

	    // Asserções básicas
	    assertNotNull(people);
	    assertFalse(people.isEmpty(), "A lista de pessoas não deveria estar vazia");
	    assertTrue(people.size() <= 12, "O tamanho da página deveria respeitar o size definido");

	    // Verifica posições específicas (0 e 4)
	    PersonDTO first = people.get(0);
	    
	    assertNotNull(first.getId());
	    assertNotNull(first.getFirstName());
	    assertNotNull(first.getLastName());
	    
	    assertTrue(first.getId() > 0);
	    
	    assertEquals("Alvie", first.getFirstName());
	    assertEquals("Basden", first.getLastName());
	    assertEquals("14th Floor", first.getAddress());
	    assertEquals("Male", first.getGender());
	    assertFalse(first.getEnabled());
	    
	    PersonDTO fifth = people.get(4);

	    assertNotNull(fifth.getId());
	    assertNotNull(fifth.getFirstName());
	    assertNotNull(fifth.getLastName());
	    
	    assertTrue(fifth.getId() > 0);
	    
	    assertEquals("Alyson", fifth.getFirstName());
	    assertEquals("Muge", fifth.getLastName());
	    assertEquals("Room 397", fifth.getAddress());
	    assertEquals("Female", fifth.getGender());
	    assertFalse(fifth.getEnabled());
	    
	}
	
	@Test
	@Order(7)
	void testFindByName() throws JsonProcessingException {
	
		// Chamada GET no endpoint com paginação
		String content = given(specification)
				.accept(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("firstName", "and")
				.queryParams("page", 0, "size", 12, "direction", "asc")
				.when()
				.get("findPeopleByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();
		
		// Converte o JSON para EmbeddedResponse<PersonDTO>
		EmbeddedResponsePersonJson<PersonDTO> response = objectMapper.readValue(
				content, new TypeReference<EmbeddedResponsePersonJson<PersonDTO>>() {});
		
		// Extrai a lista de pessoas dentro de "_embedded"
		List<PersonDTO> people = response.getEmbedded().getPeople();
		
		// Asserções básicas
		assertNotNull(people);
		assertFalse(people.isEmpty(), "A lista de pessoas não deveria estar vazia");
		assertTrue(people.size() <= 12, "O tamanho da página deveria respeitar o size definido");
		
		// Verifica posições específicas (0 e 4)
		PersonDTO first = people.get(0);
		
		assertNotNull(first.getId());
		assertNotNull(first.getFirstName());
		assertNotNull(first.getLastName());
		
		assertTrue(first.getId() > 0);
		
		assertEquals("Alexandrina", first.getFirstName());
		assertEquals("Hindrick", first.getLastName());
		assertEquals("3rd Floor", first.getAddress());
		assertEquals("Female", first.getGender());
		assertFalse(first.getEnabled());
		
		PersonDTO fifth = people.get(4);
		
		assertNotNull(fifth.getId());
		assertNotNull(fifth.getFirstName());
		assertNotNull(fifth.getLastName());
		
		assertTrue(fifth.getId() > 0);
		
		assertEquals("Andrus", fifth.getFirstName());
		assertEquals("Jestico", fifth.getLastName());
		assertEquals("Room 1110", fifth.getAddress());
		assertEquals("Male", fifth.getGender());
		assertTrue(fifth.getEnabled());
		
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
