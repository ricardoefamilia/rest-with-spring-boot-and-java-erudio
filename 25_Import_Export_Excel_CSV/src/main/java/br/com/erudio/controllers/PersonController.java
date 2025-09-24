package br.com.erudio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.erudio.controllers.docs.PersonControllerDocs;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.exporter.MediaTypes;
import br.com.erudio.services.PersonServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

// @CrossOrigin(origins = "http://localhost:8080") // add Cros no application.yml
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController implements PersonControllerDocs {

	@Autowired
	private PersonServices service; //injetando a partir do @Service no PersonService
	//private PersonServices service new PersonService(); sem usar @Service no PersonService
	
	@Override
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0" ) Integer page,
			@RequestParam(value = "size", defaultValue = "12" ) Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findAll(pageable));
	}
	
	@Override
	@GetMapping(value = "/exportPage", produces= {
			MediaTypes.APPLICATION_XLSX_VALUE, 
			MediaTypes.APPLICATION_CSV_VALUE
			})
	public ResponseEntity<Resource> exportPage(
			@RequestParam(value = "page", defaultValue = "0" ) Integer page,
			@RequestParam(value = "size", defaultValue = "12" ) Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction,
			HttpServletRequest request
			) {
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		
		String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
		
		Resource file = service.exportPage(pageable, acceptHeader);

		var contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";
		var fileExtension = MediaTypes.APPLICATION_XLSX_VALUE.equalsIgnoreCase(acceptHeader) ? ".xlsx" : ".csv";
		var filename = "people_exported" + fileExtension;
		
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
				.body(file);
		
	}
	
	@Override
	@GetMapping(value = "/findPeopleByName/{firstName}",
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findByName(
			@PathVariable("firstName") String firstName,
			@RequestParam(value = "page", defaultValue = "0" ) Integer page,
			@RequestParam(value = "size", defaultValue = "12" ) Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			){
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"));
		return ResponseEntity.ok(service.findByName(firstName, pageable));
	}
	
//	@CrossOrigin(origins = "http://localhost:8080") // add Cros no application.yml
	@Override
	@GetMapping(value = "/{id}", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	public PersonDTO findById(@PathVariable("id") Long id) {
		return service.findById(id);
	}
	
//	@CrossOrigin(origins = "http://localhost:8080") // add Cros no application.yml
	@PostMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	@Override
	public PersonDTO create(@RequestBody PersonDTO person) {
		return service.create(person);
	}
	
	@PostMapping(
			value = "/massCreation",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
			)
	@Override
	public List<PersonDTO> massCreation(@RequestParam("file") MultipartFile file) {
		return service.massCreation(file);
	}
	
	
	
	@PutMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	@Override
	public PersonDTO update(@RequestBody PersonDTO person) {
		return service.update(person);
	}
	
	
	@PatchMapping(value = "/{id}", 
			produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	@Override
	public PersonDTO disablePerson(@PathVariable Long id) {
		return service.disablePerson(id);
	}
	
	
	@DeleteMapping(value = "/{id}")
	@Override
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
