package br.com.erudio.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exception.UnsupportedMathOperationException;
import br.com.erudio.math.SimpleMath;
import br.com.erudio.utils.NumberConverter;
import br.com.erudio.utils.NumberValidation;

@RestController
@RequestMapping("/math")
public class MathController {

	private NumberConverter nc = new NumberConverter();
	private NumberValidation nv = new NumberValidation();
	private SimpleMath sm = new SimpleMath();
	
	
	// http://localhost:8080/math/sum/3/5
	@RequestMapping("/sum/{numberOne}/{numberTwo}")
	public Double sum(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo
			) throws Exception{
		if(!nv.isNumeric(numberOne) || !nv.isNumeric(numberTwo)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		return sm.sum(nc.convertToDouble(numberOne), nc.convertToDouble(numberTwo));
	}

	
	// http://localhost:8080/math/subtraction/3/5
	@RequestMapping("/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo
			) throws Exception{
		if(!nv.isNumeric(numberOne) || !nv.isNumeric(numberTwo)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		return sm.subtraction(nc.convertToDouble(numberOne), nc.convertToDouble(numberTwo));
	}
	
	// http://localhost:8080/math/division/3/5
	@RequestMapping("/division/{numberOne}/{numberTwo}")
	public Double division(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo
			) throws Exception{
		if(!nv.isNumeric(numberOne) || !nv.isNumeric(numberTwo)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico válido");
		if(numberTwo.equalsIgnoreCase("0"))
			throw new UnsupportedMathOperationException("Divisão impossível de se resolver. O denominador não pode ser zero.");
		return sm.division(nc.convertToDouble(numberOne), nc.convertToDouble(numberTwo));
	}
	
	// http://localhost:8080/math/multiplication/3/5
	@RequestMapping("/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo
			) throws Exception{
		if(!nv.isNumeric(numberOne) || !nv.isNumeric(numberTwo)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		return sm.multiplication(nc.convertToDouble(numberOne), nc.convertToDouble(numberTwo));
	}
	
	// http://localhost:8080/math/mean/3/5
	@RequestMapping("/mean/{numberOne}/{numberTwo}")
	public Double mean(
			@PathVariable("numberOne") String numberOne, 
			@PathVariable("numberTwo")  String numberTwo
			) throws Exception{
		if(!nv.isNumeric(numberOne) || !nv.isNumeric(numberTwo)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		return sm.mean(nc.convertToDouble(numberOne), nc.convertToDouble(numberTwo));
	}
	
	// http://localhost:8080/math/squareRoot/3/5
	@RequestMapping("/squareRoot/{number}")
	public Double squareRoot(
			@PathVariable("number") String number
			) throws Exception{
		if(!nv.isNumeric(number)) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		return sm.squareRoot(nc.convertToDouble(number));
	}
	
}
