package br.com.erudio.controllers.docs;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import br.com.erudio.data.dto.BookDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface BookControllerDocs {

	@Operation(
			summary = "Find All Books", 
			description = "Find All Books",
			tags = {"Books"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = {
									@Content(
										mediaType = MediaType.APPLICATION_JSON_VALUE,
										array = @ArraySchema(schema = @Schema(implementation = BookDTO.class))
									)
							}),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	List<BookDTO> findAll();

	@Operation(
			summary = "Find a book", 
			description = "Find a specific book by your ID",
			tags = {"Books"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = BookDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	BookDTO findById(Long id);

	@Operation(
			summary = "Add a book", 
			description = "Add a specific book",
			tags = {"Books"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = BookDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	BookDTO create(BookDTO book);

	@Operation(
			summary = "Update a book", 
			description = "Update a specific book",
			tags = {"Books"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = BookDTO.class))
					),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	BookDTO update(BookDTO book);

	@Operation(
			summary = "Delete a book", 
			description = "Delete a specific book by ID",
			tags = {"Books"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = @Content(schema = @Schema(implementation = BookDTO.class))
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
