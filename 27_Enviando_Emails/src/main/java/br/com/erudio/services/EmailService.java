package br.com.erudio.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
}
