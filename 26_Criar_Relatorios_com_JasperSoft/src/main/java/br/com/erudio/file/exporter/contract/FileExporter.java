package br.com.erudio.file.exporter.contract;

import java.io.InputStream;
import java.util.List;

import org.springframework.core.io.Resource;

import br.com.erudio.data.dto.PersonDTO;

public interface FileExporter {

	Resource exportFile(List<PersonDTO> people) throws Exception;
	
}
