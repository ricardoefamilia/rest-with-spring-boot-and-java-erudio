package br.com.erudio.file.importer.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.importer.contract.FileImporter;

@Component
public class XlsxImporter implements FileImporter{

	@Override
	public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
		try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			
			if(rowIterator.hasNext()) rowIterator.next();
			
			return parseRowsToPersonDTOList(rowIterator);
		}
	}

	private List<PersonDTO> parseRowsToPersonDTOList(Iterator<Row> rowIterator) {
		List<PersonDTO> people = new ArrayList<>();
		
		while(rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if(isRowValid(row)) {
				people.add(parseRowToPersonDTO(row));
			}
		}
		
		return people;
	}

	private PersonDTO parseRowToPersonDTO(Row row) {
		 PersonDTO person = new PersonDTO();
         person.setFirstName(row.getCell(0).getStringCellValue());
         person.setLastName(row.getCell(1).getStringCellValue());
         person.setAddress(row.getCell(2).getStringCellValue());
         person.setGender(row.getCell(3).getStringCellValue());
         person.setEnabled(true);
		return person;
	}

	private boolean isRowValid(Row row) {
		return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
	}

}
