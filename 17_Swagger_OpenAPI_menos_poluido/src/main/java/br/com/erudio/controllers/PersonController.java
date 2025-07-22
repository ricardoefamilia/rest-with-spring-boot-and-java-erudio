package br.com.erudio.controllers;

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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {

	@Autowired
	private PersonServices service; //injetando a partir do @Service no PersonService
	//private PersonServices service new PersonService(); sem usar @Service no PersonService
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	@Operation(
		summary = "Find All People", 
		description = "Find All People",
		tags = {"People"},
		responses = {
				@ApiResponse(
						description = "Success", 
						responseCode = "200", 
						content = {
								@Content(
									mediaType = MediaType.APPLICATION_JSON_VALUE,
									array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
								)
						}),
				@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
				@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
				@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
				@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
				@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
		}
	)
	public List<PersonDTO> findAll() {
		return service.findAll();
	}
	
	@GetMapping(value = "/{id}", produces= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
	@Operation(
			summary = "Find a Person", 
			description = "Find a specific person by your ID",
			tags = {"People"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = PersonDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
		)
	public PersonDTO findById(@PathVariable("id") Long id) {
		return service.findById(id);
	}
	
	@PostMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	@Operation(
			summary = "Add a Person", 
			description = "Add a specific person",
			tags = {"People"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = PersonDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
		)
	public PersonDTO create(@RequestBody PersonDTO person) {
		return service.create(person);
	}
	
	@PutMapping(
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
		produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
	)
	@Operation(
			summary = "Update a Person", 
			description = "Update a specific person",
			tags = {"People"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = PersonDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
		)
	public PersonDTO update(@RequestBody PersonDTO person) {
		return service.update(person);
	}
	
	@DeleteMapping(value = "/{id}")
	@Operation(
			summary = "Delete a Person", 
			description = "Delete a specific person by ID",
			tags = {"People"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = PersonDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
		)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
