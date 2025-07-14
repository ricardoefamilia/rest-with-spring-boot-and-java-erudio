package br.com.erudio.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import br.com.erudio.model.Person;

@Service
public class PersonServices {

	private final AtomicLong counter = new AtomicLong();
	
	private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());
	
	public Person findById(String id) {
		logger.info("Finding one Person!");
		
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Ricardo");
		person.setLastName("Amaral");
		person.setAddress("Brasília - Distrito Federal - Brasil");
		person.setGender("Male");
		
		return person;
	}
	
	public List<Person> findAll() {
		logger.info("Finding all people!");
		
		List<Person> persons = new ArrayList<Person>();
		for(int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);
		}
		return persons;
	}

	private Person mockPerson(int i) {
		Person person = new Person();
		person.setId(counter.incrementAndGet());
		person.setFirstName("Firtname " + i);
		person.setLastName("Lastname " + i);
		person.setAddress("Some Address in Brazil");
		person.setGender("Male");
		
		return person;
	}
	
	public Person create(Person person) {
		logger.info("Creating one Person!");
		
		return person;
	}
	
	public Person update(Person person) {
		logger.info("Updating one Person!");
		
		return person;
	}
	
	public void delete(String id) {
		logger.info("Deleting one Person!");
	}
	
}
