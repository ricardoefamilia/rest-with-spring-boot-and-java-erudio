package br.com.erudio.services;


import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.config.EmailConfig;
import br.com.erudio.data.dto.request.EmailRequestDTO;
import br.com.erudio.mail.EmailSender;

@Service
public class EmailService {

	@Autowired
	private EmailSender emailSender;
	
	@Autowired
	private EmailConfig emailConfigs;
	
	public void sendSimpleEmail(EmailRequestDTO emailRequest) {
		emailSender
			.to(emailRequest.getTo())
			.withSubject(emailRequest.getSubject())
			.withMessage(emailRequest.getBody())
			.send(emailConfigs);
	}
	
	public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){
		File tempFile = null;
		try {
			EmailRequestDTO emailRequest = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
			tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
			attachment.transferTo(tempFile);
			
			emailSender
				.to(emailRequest.getTo())
				.withSubject(emailRequest.getSubject())
				.withMessage(emailRequest.getBody())
				.attach(tempFile.getAbsolutePath())
				.send(emailConfigs);
			
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Error parsing email request JSON!", e);
		} catch (IOException e) {
			throw new RuntimeException("Error processing the attachment!", e);
		}finally {
			if(tempFile != null && tempFile.exists()) tempFile.delete();
		}
	}
	
}
