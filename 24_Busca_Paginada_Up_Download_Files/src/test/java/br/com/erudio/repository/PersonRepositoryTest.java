package br.com.erudio.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;

@ExtendWith(SpringExtension.class) //integra o spring framework com o JUnit 5 para uso de componentes, bins e recursos configurados no ambiente de testes - fundamental para testes que dependem do spring content
@DataJpaTest //configura o teste para trabalhar com JPA - camada de persistência (repository, model (entity), banco de dados (h2, test containers, outros bd...)
@AutoConfigureTestDatabase(replace = Replace.NONE) //replace none bloqueia acesso ao bd h2 nativo e dá oportunidade para utilizar bd real utilizado na app
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //ordena a execução dos testes, principalmente quando o estado de um teste depende de outro
class PersonRepositoryTest extends AbstractIntegrationTest{
	
	@Autowired
	PersonRepository repository;
	
	private static Person person;

	@BeforeAll
	static void setUp() throws Exception {
		person = new Person();
	}
	
	@Test
	@Order(1)
	void testFindPeopleByName() {
		Pageable pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"));
		
		person = repository.findPeopleByName("ardo", pageable).getContent().get(2);
		
		assertNotNull(person);
		assertNotNull(person.getId());
		
		assertEquals("Ricardo", person.getFirstName());
		assertEquals("Amaral", person.getLastName());
		assertEquals("Brasília - Brasil", person.getAddress());
		assertEquals("Male", person.getGender());
		assertTrue(person.getEnabled());
	}

	@Test
	@Order(2)
	void testDisablePerson() {
		
		Long id = person.getId();
		
		// tornando False o campo Enabled 
		repository.disablePerson(id);
		
		var result = repository.findById(id);
		
		person = result.get();

		assertNotNull(person);
		assertNotNull(person.getId());
		
		assertEquals("Ricardo", person.getFirstName());
		assertEquals("Amaral", person.getLastName());
		assertEquals("Brasília - Brasil", person.getAddress());
		assertEquals("Male", person.getGender());
		//verificando se tornou False o campo Enabled
		assertFalse(person.getEnabled());
	}

}
