package br.com.erudio.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test/v1")
public class TesteLogController {
	
	private Logger logger = LoggerFactory.getLogger(TesteLogController.class.getName());

	@GetMapping("/test")
	public String testLog() {
		logger.trace("This is an TRACE log");
		logger.debug("This is an DEBUG log");
		logger.info("This is an INFO log");
		logger.warn("This is an WARN log");
		logger.error("This is an ERROR log");
		
		return "Logs generated successfully!";
	}
}
