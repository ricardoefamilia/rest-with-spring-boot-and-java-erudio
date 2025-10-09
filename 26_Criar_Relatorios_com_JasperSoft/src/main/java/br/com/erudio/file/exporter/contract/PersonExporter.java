package br.com.erudio.file.exporter.contract;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;

import br.com.erudio.data.dto.PersonDTO;

public interface PersonExporter {

	Resource exportPeople(List<PersonDTO> people) throws Exception;
	Resource exportPerson(PersonDTO person) throws Exception;
	
}
