package br.com.erudio.math;

public class SimpleMath {

	public Double sum(Double numberOne, Double numberTwo){
		return numberOne + numberTwo;
	}
	public Double subtraction(Double numberOne, Double numberTwo){
		return numberOne - numberTwo;
	}
	public Double multiplication(Double numberOne, Double numberTwo){
		return numberOne * numberTwo;
	}
	public Double division(Double numberOne, Double numberTwo){
		Double resp = 0D;
		if(numberTwo != 0) {
			resp = numberOne / numberTwo;
		}
		return resp;
	}
	public Double mean(Double numberOne, Double numberTwo){
		return (numberOne + numberTwo) / 2;
	}
	public Double squareRoot(Double number){
		return Math.sqrt(number);
	}
}
