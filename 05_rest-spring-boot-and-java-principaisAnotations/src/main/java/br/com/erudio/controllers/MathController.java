package br.com.erudio.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

	// http://localhost:8080/math/sum/3/5
	@RequestMapping("/sum/{numberOne}/{numberTwo}")
	public Double sum(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo) {
		
		return 1D;
	}
	
	// http://localhost:8080/math/subtraction/3/5
	
	
	// http://localhost:8080/math/division/3/5
	
	
	// http://localhost:8080/math/multiplication/3/5
	
	
}
