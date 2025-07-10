package br.com.erudio.utils;

public class NumberValidation {
	
	public boolean isNumeric(String strNumber) {
		if(strNumber == null || strNumber.isEmpty()) return false;
		String number = strNumber.replace(",", ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");
		
	}
}
