package br.com.erudio.integrationtests.controllers.withyaml;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.controllers.withyaml.mapper.YAMLMapper;
import br.com.erudio.integrationtests.dto.AccountCredentialsDTO;
import br.com.erudio.integrationtests.dto.BookDTO;
import br.com.erudio.integrationtests.dto.TokenDTO;
import br.com.erudio.integrationtests.response.xml_yaml.PagedModelBook;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerYamlTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static YAMLMapper objectMapper;
	private static BookDTO book;
	private static TokenDTO tokenDto;

	@BeforeAll
	static void setUp() {
		objectMapper = new YAMLMapper();

		book = new BookDTO();
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
				.setBasePath("/api/book")
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
		mockBook();

		var createdBook = given().config(
				RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
					.body(book, objectMapper)
				.when()
					.post()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
					.body()
						.as(BookDTO.class, objectMapper);

		book = createdBook;

		assertNotNull(createdBook.getId());

		assertTrue(createdBook.getId() > 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdBook.getLaunchDate());
		assertEquals("Teste Author", createdBook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdBook.getPrice());
		assertEquals("Teste Title", createdBook.getTitle());
		
	}
	
	@Test
	@Order(2)
	void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Teste Title");
		
		var createdBook = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
				.body(book, objectMapper)
				.when()
				.put()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
				.body()
				.as(BookDTO.class, objectMapper);

		book = createdBook;
		
		assertNotNull(createdBook.getId());
		
		assertTrue(createdBook.getId() > 0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdBook.getLaunchDate());
		assertEquals("Teste Author", createdBook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdBook.getPrice());
		assertEquals("Teste Title", createdBook.getTitle());
		
	}
	
	@Test
	@Order(3)
	void testFindById() throws JsonMappingException, JsonProcessingException {

		var createdBook = given().config(
					RestAssuredConfig.config()
					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
				)
				.spec(specification)
				.contentType(MediaType.APPLICATION_YAML_VALUE)
				.accept(MediaType.APPLICATION_YAML_VALUE)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_YAML_VALUE)
				.extract()
				.body()
				.as(BookDTO.class, objectMapper);

		book = createdBook;

		assertNotNull(createdBook.getId());

		assertTrue(createdBook.getId() > 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdBook.getLaunchDate());
		assertEquals("Teste Author", createdBook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdBook.getPrice());
		assertEquals("Teste Title", createdBook.getTitle());

	}
	
//	@Test
//	@Order(4)
//	void testDisable() throws JsonMappingException, JsonProcessingException {
//		
//		var createdBook = given().config(
//					RestAssuredConfig.config()
//					.encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(
//							MediaType.APPLICATION_YAML_VALUE, ContentType.TEXT))
//				)
//				.spec(specification)
//				.accept(MediaType.APPLICATION_YAML_VALUE)
//				.pathParam("id", book.getId())
//				.when()
//				.patch("{id}")
//				.then()
//					.statusCode(200)
//					.contentType(MediaType.APPLICATION_YAML_VALUE)
//				.extract()
//				.body()
//				.as(BookDTO.class, objectMapper);
//		
//		book = createdBook;
//		
//		assertNotNull(createdBook.getId());
//		
//		assertTrue(createdBook.getId() > 0);
//		
//		assertEquals("Winston", createdBook.getAuthor());
//		assertEquals("Leonard Spencer-Churchill", createdBook.getLaunchDate());
//		assertEquals("Oxfordshire - England", createdBook.getPrice());
//		assertEquals("Male", createdBook.getTitle());
//		
//	}
	
	@Test
	@Order(4)
	void testDelete() throws JsonMappingException, JsonProcessingException {
		
		given(specification)
				.pathParam("id", book.getId())
				.when()
					.delete("{id}")
				.then()
					.statusCode(204);
	}
	
	@Test
	@Order(5)
	void testFindAll() throws JsonMappingException, JsonProcessingException {
	    // Chamada GET no endpoint com paginação
	    PagedModelBook response = given(specification)
	            .accept(MediaType.APPLICATION_YAML_VALUE)
	            .queryParams("page", 0, "size", 5, "direction", "asc")
	        .when()
	            .get()
	        .then()
	            .statusCode(200)
	            .contentType(MediaType.APPLICATION_YAML_VALUE)
	        .extract()
	            .body()
	            .as(PagedModelBook.class, objectMapper);

	    // Extrai a lista de pessoas dentro de "content"
	    List<BookDTO> people = response.getContent();

	    assertNotNull(people);
	    assertFalse(people.isEmpty(), "A lista de pessoas não deveria estar vazia");
	    assertTrue(people.size() <= 5, "O tamanho da página deveria respeitar o size definido");

	    BookDTO first = people.get(0);

	    assertNotNull(first.getId());
	    assertNotNull(first.getAuthor());
	    assertNotNull(first.getLaunchDate());

	    System.out.println("✅ Página retornada: " + people.size() + " registros");
	    System.out.println("Posição 0: " + first.getAuthor() + " " + first.getLaunchDate());
	    
	    assertTrue(first.getId() > 0);

	    SimpleDateFormat sdffirst = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStrfirst = sdffirst.format(first.getLaunchDate());
	    assertEquals("Craig Larman", first.getAuthor());
	    assertEquals("2018-08-14", launchDateStrfirst);
	    assertEquals(72.89, first.getPrice());
	    assertEquals("Agile and Iterative Development: A Manager’s Guide", first.getTitle());

	}

	private void mockBook() {
		try {
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	        book.setAuthor("Teste Author");
	        book.setLaunchDate(sdf.parse("2023-03-10"));
	        book.setPrice(49.90);
	        book.setTitle("Teste Title");
	    } catch (ParseException e) {
	        throw new RuntimeException(e);
	    }
	}
}
