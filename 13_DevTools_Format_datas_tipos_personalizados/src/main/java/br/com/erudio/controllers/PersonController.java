package br.com.erudio.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.services.PersonServices;

@RestController
@RequestMapping("/api/person/v1")
public class PersonController {

	@Autowired
	private PersonServices service; //injetando a partir do @Service no PersonService
	//private PersonServices service new PersonService(); sem usar @Service no PersonService
	
	@GetMapping(produces= MediaType.APPLICATION_JSON_VALUE)
	public List<PersonDTO> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value = "/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
	public PersonDTO findById(@PathVariable("id") Long id) {
		var person = service.findById(id);
		//exemplo de data para rodar o projeto
		person.setBirthDay(new Date());
		//person.setPhoneNumber("+55 (61)99888-7777");
		person.setPhoneNumber("");
		person.setLastName(null);
		person.setSensitiveData("Cafeteria");
		return person;
	}
	
	@PostMapping(
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public PersonDTO create(@RequestBody PersonDTO person) {
		return service.create(person);
	}
	
	@PutMapping(
		consumes = MediaType.APPLICATION_JSON_VALUE,
		produces = MediaType.APPLICATION_JSON_VALUE
	)
	public PersonDTO update(@RequestBody PersonDTO person) {
		return service.update(person);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
