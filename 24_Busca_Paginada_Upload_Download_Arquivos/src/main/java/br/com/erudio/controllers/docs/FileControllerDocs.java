package br.com.erudio.controllers.docs;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import br.com.erudio.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@Tag(name = "File Endpoint")
public interface FileControllerDocs {

	@Operation(
			summary = "Upload of the File", 
			description = "Upload of the File",
			tags = {"File"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = {
									@Content(
										mediaType = MediaType.APPLICATION_JSON_VALUE,
										array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDTO.class))
									)
							}),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	UploadFileResponseDTO uploadFile(MultipartFile file);
	
	@Operation(
			summary = "Upload All Files", 
			description = "Upload All Files",
			tags = {"Files"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = {
									@Content(
										mediaType = MediaType.APPLICATION_JSON_VALUE,
										array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDTO.class))
									)
							}),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);
	
	@Operation(
			summary = "Download the File", 
			description = "Download the File",
			tags = {"File"},
			responses = {
					@ApiResponse(
							description = "Success", 
							responseCode = "200", 
							content = {
									@Content(
										mediaType = MediaType.APPLICATION_JSON_VALUE,
										array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDTO.class))
									)
							}),
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Servr Error", responseCode = "500", content = @Content)
			}
	)
	ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);
	
}
