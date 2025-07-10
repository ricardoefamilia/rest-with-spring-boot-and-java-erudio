package br.com.erudio.utils;

import br.com.erudio.exception.UnsupportedMathOperationException;

public class NumberConverter {

	public Double convertToDouble(String strNumber) {
		
		if(strNumber == null || strNumber.isEmpty()) 
			throw new UnsupportedMathOperationException("Por favor, entre com um valor numérico");
		String number = strNumber.replace(",", ".");

		return Double.parseDouble(number);
	}
}
