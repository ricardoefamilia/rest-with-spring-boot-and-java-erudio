package br.com.erudio.integrationtests.controllers.withjson;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import br.com.erudio.integrationtests.dto.BookDTO;
import br.com.erudio.integrationtests.response.json.EmbeddedResponseBookJson;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerJsonTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static BookDTO book;

	@BeforeAll
	static void setUp() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		book = new BookDTO();
	}

	@Test
	@Order(1)
	void testCreate() throws JsonMappingException, JsonProcessingException {
		mockbook();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
				.setBasePath("/api/book")
				.setPort(port) // porta dinâmica
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
					.body(book)
				.when()
					.post()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
					.body()
						.asString();

		BookDTO createdbook = objectMapper.readValue(content, BookDTO.class);
		book = createdbook;

		assertNotNull(createdbook.getId());

		assertTrue(createdbook.getId() > 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdbook.getLaunchDate());
		assertEquals("Teste Author", createdbook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdbook.getPrice());
		assertEquals("Teste Title", createdbook.getTitle());
		
	}
	
	@Test
	@Order(2)
	void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setTitle("Teste Title");
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.when()
				.put()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();
		
		BookDTO createdbook = objectMapper.readValue(content, BookDTO.class);
		book = createdbook;
		
		assertNotNull(createdbook.getId());
		
		assertTrue(createdbook.getId() > 0);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdbook.getLaunchDate());
		assertEquals("Teste Author", createdbook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdbook.getPrice());
		assertEquals("Teste Title", createdbook.getTitle());
		
	}
	
	@Test
	@Order(3)
	void testFindById() throws JsonMappingException, JsonProcessingException {

		var content = given(specification)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				.extract()
				.body()
				.asString();

		BookDTO createdbook = objectMapper.readValue(content, BookDTO.class);
		book = createdbook;

		assertNotNull(createdbook.getId());

		assertTrue(createdbook.getId() > 0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStr = sdf.format(createdbook.getLaunchDate());
		assertEquals("Teste Author", createdbook.getAuthor());
		assertEquals("2023-03-10", launchDateStr);
		assertEquals(49.90, createdbook.getPrice());
		assertEquals("Teste Title", createdbook.getTitle());
	
	}
	
//	@Test
//	@Order(4)
//	void testDisable() throws JsonMappingException, JsonProcessingException {
//		
//		var content = given(specification)
//				.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.pathParam("id", book.getId())
//				.when()
//				.patch("{id}")
//				.then()
//					.statusCode(200)
//					.contentType(MediaType.APPLICATION_JSON_VALUE)
//				.extract()
//				.body()
//				.asString();
//		
//		BookDTO createdbook = objectMapper.readValue(content, BookDTO.class);
//		book = createdbook;
//		
//		assertNotNull(createdbook.getId());
//		
//		assertTrue(createdbook.getId() > 0);
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String launchDateStr = sdf.format(createdbook.getLaunchDate());
//		assertEquals("Teste Author", createdbook.getAuthor());
//		assertEquals("2023-03-10", launchDateStr);
//		assertEquals(49.90, createdbook.getPrice());
//		assertEquals("Teste Title", createdbook.getTitle());
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
	@DisplayName("Deve retornar a página 0 e validar posições específicas")
	void testFindAllbooksWithPagination() throws JsonProcessingException {
		
		// Configuração da especificação da requisição REST Assured
	    specification = new RequestSpecBuilder()
	            .setBasePath("/api/book")
	            .setPort(port)
	            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	            .build();

	    // Chamada GET no endpoint com paginação
	    String content = given(specification)
	            .accept(MediaType.APPLICATION_JSON_VALUE)
	            .queryParams("page", 0, "size", 5, "direction", "asc")
	        .when()
	            .get()
	        .then()
	            .statusCode(200)
	        .extract()
	            .body()
	            .asString();

	    // Converte o JSON para EmbeddedResponse<BookDTO>
	    EmbeddedResponseBookJson<BookDTO> response = objectMapper.readValue(
	            content, new TypeReference<EmbeddedResponseBookJson<BookDTO>>() {});

	    // Extrai a lista de pessoas dentro de "_embedded"
	    List<BookDTO> books = response.getEmbedded().getBooks();

	    // Asserções básicas
	    assertNotNull(books);
	    assertFalse(books.isEmpty(), "A lista de pessoas não deveria estar vazia");
	    assertTrue(books.size() <= 5, "O tamanho da página deveria respeitar o size definido");

	    // Verifica posições específicas (0)
	    BookDTO first = books.get(0);
	    
	    assertNotNull(first.getId());
	    assertNotNull(first.getAuthor());
	    assertNotNull(first.getLaunchDate());
	    
	    assertTrue(first.getId() > 0);
	    
	    SimpleDateFormat sdffirst = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStrfirst = sdffirst.format(first.getLaunchDate());
		assertEquals("Craig Larman", first.getAuthor());
	    assertEquals("2018-08-14", launchDateStrfirst);
	    assertEquals(72.89, first.getPrice());
	    assertEquals("Agile and Iterative Development: A Manager’s Guide", first.getTitle());
	}

	private void mockbook() {
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
