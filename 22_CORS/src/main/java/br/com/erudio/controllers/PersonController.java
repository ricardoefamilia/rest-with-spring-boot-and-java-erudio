package br.com.erudio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.controllers.docs.PersonControllerDocs;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;

// @CrossOrigin(origins = "http://localhost: 8080") // add Cros no application.yml
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

	@Autowired
	private PersonServices service; //injetando a partir do @Service no PersonService
	//private PersonServices service new PersonService(); sem usar @Service no PersonService
	
	@Override
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	public List<PersonDTO> findAll() {
		return service.findAll();
	}
	
//	@CrossOrigin(origins = "http://localhost: 8080") // add Cros no application.yml
	@Override
	@GetMapping(value = "/{id}", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	public PersonDTO findById(@PathVariable("id") Long id) {
		return service.findById(id);
	}
	
//	@CrossOrigin(origins = "http://localhost: 8080") // add Cros no application.yml
	@Override
	@PostMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	public PersonDTO create(@RequestBody PersonDTO person) {
		return service.create(person);
	}
	
	@Override
	@PutMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	public PersonDTO update(@RequestBody PersonDTO person) {
		return service.update(person);
	}
	
	@Override
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
