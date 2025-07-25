package br.com.erudio.controllers.docs;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.erudio.data.dto.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface PersonControllerDocs {

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
	List<PersonDTO> findAll();

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
	PersonDTO findById(Long id);

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
	PersonDTO create(PersonDTO person);

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
	PersonDTO update(PersonDTO person);

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
	ResponseEntity<?> delete(Long id);

}