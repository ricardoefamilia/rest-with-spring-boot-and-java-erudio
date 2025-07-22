package br.com.erudio.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.unittests.mapper.mocks.MockPerson;

//os objetos instanciados durarão apenas para esta classe
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServicesTest {
	
	MockPerson input;
	
	@InjectMocks
	private PersonServices service;
	
	@Mock
	PersonRepository repository;
	
	@BeforeEach
	void setUp() {
		input = new MockPerson();
		MockitoAnnotations.openMocks(this);
	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testFindAll() {
		List<Person> list = input.mockEntityList();
		when(repository.findAll()).thenReturn(list);
		List<PersonDTO> people = service.findAll();
		
		assertNotNull(people);
		assertEquals(14, people.size());
		
		var personOne = people.get(1);
		
		assertNotNull(personOne);
		assertNotNull(personOne.getId());
		assertNotNull(personOne.getFirstName());
		assertNotNull(personOne.getLastName());
		assertNotNull(personOne.getAddress());
		assertNotNull(personOne.getGender());
		
		assertNotNull(personOne.getLinks());
		assertNotNull(personOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(personOne.getLinks());
		assertNotNull(personOne.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(personOne.getLinks());
		assertNotNull(personOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(personOne.getLinks());
		assertNotNull(personOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(personOne.getLinks());
		assertNotNull(personOne.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test1", personOne.getAddress());
		assertEquals("First Name Test1", personOne.getFirstName());
		assertEquals("Last Name Test1", personOne.getLastName());
		assertEquals("Female", personOne.getGender());
		
		var personFour = people.get(4);
		
		assertNotNull(personFour);
		assertNotNull(personFour.getId());
		assertNotNull(personFour.getFirstName());
		assertNotNull(personFour.getLastName());
		assertNotNull(personFour.getAddress());
		assertNotNull(personFour.getGender());
		
		assertNotNull(personFour.getLinks());
		assertNotNull(personFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/4")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(personFour.getLinks());
		assertNotNull(personFour.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(personFour.getLinks());
		assertNotNull(personFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(personFour.getLinks());
		assertNotNull(personFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(personFour.getLinks());
		assertNotNull(personFour.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/4")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test4", personFour.getAddress());
		assertEquals("First Name Test4", personFour.getFirstName());
		assertEquals("Last Name Test4", personFour.getLastName());
		assertEquals("Male", personFour.getGender());
		
		var personSeven = people.get(7);
		
		assertNotNull(personSeven);
		assertNotNull(personSeven.getId());
		assertNotNull(personSeven.getFirstName());
		assertNotNull(personSeven.getLastName());
		assertNotNull(personSeven.getAddress());
		assertNotNull(personSeven.getGender());
		
		assertNotNull(personSeven.getLinks());
		assertNotNull(personSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/7")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(personSeven.getLinks());
		assertNotNull(personSeven.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(personSeven.getLinks());
		assertNotNull(personSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(personSeven.getLinks());
		assertNotNull(personSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(personSeven.getLinks());
		assertNotNull(personSeven.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/7")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test7", personSeven.getAddress());
		assertEquals("First Name Test7", personSeven.getFirstName());
		assertEquals("Last Name Test7", personSeven.getLastName());
		assertEquals("Female", personSeven.getGender());
	}

	@Test
	void testFindById() {
		Person person = input.mockEntity(1);
		person.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		
		var result = service.findById(1L);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getFirstName());
		assertNotNull(result.getLastName());
		assertNotNull(result.getAddress());
		assertNotNull(result.getGender());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}

	@Test
	void testCreate() {
		Person person = input.mockEntity(1);
		Person persisted = person;
		persisted.setId(1L);
		
		PersonDTO dto = input.mockDTO(1);
		
		when(repository.save(person)).thenReturn(persisted);
		
		var result = service.create(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getFirstName());
		assertNotNull(result.getLastName());
		assertNotNull(result.getAddress());
		assertNotNull(result.getGender());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}
	
	@Test
	void testCreateWhithNullPerson() {
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
		Person person = input.mockEntity(1);
		Person persisted = person;
		persisted.setId(1L);
		
		PersonDTO dto = input.mockDTO(1);

		when(repository.findById(1L)).thenReturn(Optional.of(person));
		when(repository.save(person)).thenReturn(persisted);
		
		var result = service.update(dto);
		
		assertNotNull(result);
		assertNotNull(result.getId());
		assertNotNull(result.getFirstName());
		assertNotNull(result.getLastName());
		assertNotNull(result.getAddress());
		assertNotNull(result.getGender());
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("self")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("GET")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
				.anyMatch(link -> link.getRel().value().equals("findAll")
						&& link.getHref().endsWith("/api/person")
						&& link.getType().equals("GET")
						));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("create")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("POST")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("update")
				&& link.getHref().endsWith("/api/person")
				&& link.getType().equals("PUT")
		));
		
		assertNotNull(result.getLinks());
		assertNotNull(result.getLinks().stream()
			.anyMatch(link -> link.getRel().value().equals("delete")
				&& link.getHref().endsWith("/api/person/1")
				&& link.getType().equals("DELETE")
		));
		
		assertEquals("Address Test1", result.getAddress());
		assertEquals("First Name Test1", result.getFirstName());
		assertEquals("Last Name Test1", result.getLastName());
		assertEquals("Female", result.getGender());
	}


	@Test
	void testUpdateWhithNullPerson() {
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
		Person person = input.mockEntity(1);
		person.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		
		service.delete(1L);
		
		verify(repository, times(1)).findById(anyLong());
		verify(repository, times(1)).delete(any(Person.class));
		verifyNoMoreInteractions(repository);

	}

}
