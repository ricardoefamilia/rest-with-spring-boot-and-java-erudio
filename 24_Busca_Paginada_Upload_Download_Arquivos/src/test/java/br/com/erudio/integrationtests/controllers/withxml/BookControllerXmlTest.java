package br.com.erudio.integrationtests.controllers.withxml;

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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.BookDTO;
import br.com.erudio.integrationtests.response.xml_yaml.PagedModelBook;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookControllerXmlTest extends AbstractIntegrationTest {

	@LocalServerPort
	private int port;

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;
	private static BookDTO book;

	@BeforeAll
	static void setUp() {
		objectMapper = new XmlMapper();
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
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
					.body(book)
				.when()
					.post()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
					.body()
						.asString();

		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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
		
		var content = given(specification)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
				.body(book)
				.when()
				.put()
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
				.body()
				.asString();
		
		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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

		var content = given(specification)
				.contentType(MediaType.APPLICATION_XML_VALUE)
				.accept(MediaType.APPLICATION_XML_VALUE)
				.pathParam("id", book.getId())
				.when()
				.get("{id}")
				.then()
					.statusCode(200)
					.contentType(MediaType.APPLICATION_XML_VALUE)
				.extract()
				.body()
				.asString();

		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
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
//		var content = given(specification)
//				.accept(MediaType.APPLICATION_XML_VALUE)
//				.pathParam("id", book.getId())
//				.when()
//				.patch("{id}")
//				.then()
//					.statusCode(200)
//					.contentType(MediaType.APPLICATION_XML_VALUE)
//				.extract()
//				.body()
//				.asString();
//		
//		BookDTO createdBook = objectMapper.readValue(content, BookDTO.class);
//		book = createdBook;
//		
//		assertNotNull(createdBook.getId());
//		
//		assertTrue(createdBook.getId() > 0);
//		
//		assertEquals("Teste Author", createdBook.getAuthor());
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
	void testFindAll() throws JsonProcessingException {
	    // Configuração da especificação da requisição REST Assured
	    specification = new RequestSpecBuilder()
	            .setBasePath("/api/book")
	            .setPort(port)
	            .addFilter(new RequestLoggingFilter(LogDetail.ALL))
	            .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
	            .build();

	    // Chamada GET no endpoint com paginação
	    String content = given(specification)
	            .accept(MediaType.APPLICATION_XML_VALUE)
	            .queryParams("page", 0, "size", 5, "direction", "asc")
	        .when()
	            .get()
	        .then()
	            .statusCode(200)
	            .contentType(MediaType.APPLICATION_XML_VALUE)
	        .extract()
	            .body()
	            .asString();

	    // Converte o XML para 
	    PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);

	    // Agora usamos getbooks() diretamente
	    List<BookDTO> books = wrapper.getContent();

	    // Asserções básicas
	    assertNotNull(books);
	    assertFalse(books.isEmpty(), "A lista de pessoas não deveria estar vazia");
	    assertTrue(books.size() <= 12, "O tamanho da página deveria respeitar o size definido");

	    // Verifica posições específicas (0)
	    BookDTO first = books.get(0);

	    assertNotNull(first.getId());
	    assertNotNull(first.getAuthor());
	    assertNotNull(first.getLaunchDate());
	    
	    // Debug opcional
	    System.out.println("✅ Página retornada: " + books.size() + " registros");
	    System.out.println("Posição 0: " + first.getAuthor() + " " + first.getLaunchDate());
	    
	    assertTrue(first.getId() > 0);
	    
	    SimpleDateFormat sdffirst = new SimpleDateFormat("yyyy-MM-dd");
		String launchDateStrfirst = sdffirst.format(first.getLaunchDate());
	    assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", first.getAuthor());
	    assertEquals("2017-11-07", launchDateStrfirst);
	    assertEquals(54.0, first.getPrice());
	    assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", first.getTitle());

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
