package br.com.erudio.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.erudio.data.dto.BookDTO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;
import br.com.erudio.unittests.mapper.mocks.MockBook;

//os objetos instanciados durarão apenas para esta classe
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {
	
	MockBook input;
	
	@InjectMocks
	private BookServices service;
	
	@Mock
	BookRepository repository;
	
	@BeforeEach
	void setUp() {
		input = new MockBook();
		MockitoAnnotations.openMocks(this);
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testFindAll() {
		List<Book> list = input.mockEntityList();
		when(repository.findAll()).thenReturn(list);
		//List<BookDTO> books = new ArrayList<>(); //service.findAll();
		List<BookDTO> books = service.findAll();
		
		assertNotNull(books);
		assertEquals(14, books.size());
		
		var bookOne = books.get(1);
		
		assertNotNull(bookOne);
		assertNotNull(bookOne.getId());
		assertNotNull(bookOne.getAuthor());
		assertNotNull(bookOne.getLaunchDate());
		assertNotNull(bookOne.getPrice());
		assertNotNull(bookOne.getTitle());
		
		assertNotNull(bookOne.getLinks());
		assertNotNull(bookOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(bookOne.getLinks());
		assertNotNull(bookOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(bookOne.getLinks());
		assertNotNull(bookOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(bookOne.getLinks());
		assertNotNull(bookOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(bookOne.getLinks());
		assertNotNull(bookOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Author Test1", bookOne.getAuthor());
		assertNotNull(bookOne.getLaunchDate());
		assertEquals(25D, bookOne.getPrice());
		assertEquals("Title of the book1", bookOne.getTitle());
		
		var bookFour = books.get(4);
		
		assertNotNull(bookFour);
		assertNotNull(bookFour.getAuthor());
		assertNotNull(bookFour.getLaunchDate());
		assertNotNull(bookFour.getPrice());
		assertNotNull(bookFour.getTitle());
		
		assertNotNull(bookFour.getLinks());
		assertNotNull(bookFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/4")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(bookFour.getLinks());
		assertNotNull(bookFour.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(bookFour.getLinks());
		assertNotNull(bookFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(bookFour.getLinks());
		assertNotNull(bookFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(bookFour.getLinks());
		assertNotNull(bookFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/4")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Author Test4", bookFour.getAuthor());
		assertNotNull(bookFour.getLaunchDate());
		assertEquals(25D, bookFour.getPrice());
		assertEquals("Title of the book4", bookFour.getTitle());
		
		var bookSeven = books.get(7);
		
		assertNotNull(bookSeven);
		assertNotNull(bookSeven.getId());
		assertNotNull(bookSeven.getAuthor());
		assertNotNull(bookSeven.getLaunchDate());
		assertNotNull(bookSeven.getPrice());
		assertNotNull(bookSeven.getTitle());
		
		assertNotNull(bookSeven.getLinks());
		assertNotNull(bookSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/7")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(bookSeven.getLinks());
		assertNotNull(bookSeven.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(bookSeven.getLinks());
		assertNotNull(bookSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(bookSeven.getLinks());
		assertNotNull(bookSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(bookSeven.getLinks());
		assertNotNull(bookSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/7")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Author Test7", bookSeven.getAuthor());
		assertNotNull(bookSeven.getLaunchDate());
		assertEquals(25D, bookSeven.getPrice());
		assertEquals("Title of the book7", bookSeven.getTitle());
	}

	@Test
	void testFindById() {
		Book book = input.mockEntity(1);
		book.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertNotNull(result.getPrice());
		assertNotNull(result.getTitle());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("DELETE")
		));
		
		
		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(25D, result.getPrice());
		assertEquals("Title of the book1", result.getTitle());
	}

	@Test
	void testCreate() {
		Book book = input.mockEntity(1);
		Book persisted = book;
		persisted.setId(1L);
		
		BookDTO dto = input.mockDTO(1);
		
		when(repository.save(book)).thenReturn(persisted);
		
		var result = service.create(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertNotNull(result.getPrice());
		assertNotNull(result.getTitle());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(25D, result.getPrice());
		assertEquals("Title of the book1", result.getTitle());
	}
	
	@Test
	void testCreateWhithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, 
			() -> {
				service.create(null);
			});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	void testUpdate() {
		Book book = input.mockEntity(1);
		Book persisted = book;
		persisted.setId(1L);
		
		BookDTO dto = input.mockDTO(1);

		when(repository.findById(1L)).thenReturn(Optional.of(book));
		when(repository.save(book)).thenReturn(persisted);
		
		var result = service.update(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertNotNull(result.getPrice());
		assertNotNull(result.getTitle());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/book")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/book")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/book/1")
				&& link.getType().equals("DELETE")
		));

		assertEquals("Author Test1", result.getAuthor());
		assertNotNull(result.getLaunchDate());
		assertEquals(25D, result.getPrice());
		assertEquals("Title of the book1", result.getTitle());
	}

	@Test
	void testUpdateWhithNullBook() {
		Exception exception = assertThrows(RequiredObjectIsNullException.class, 
				() -> {
					service.update(null);
				});
		
		String expectedMessage = "It is not allowed to persist a null object";
		String actualMessage = exception.getMessage();
		
		assertTrue(actualMessage.contains(expectedMessage));
	}
	
	@Test
	void testDelete() {
		Book book = input.mockEntity(1);
		book.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(book));
		
		service.delete(1L);
		
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).delete(any(Book.class));
		verifyNoMoreInteractions(repository);

	}

}
