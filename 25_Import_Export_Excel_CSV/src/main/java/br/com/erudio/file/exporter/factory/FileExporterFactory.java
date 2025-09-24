package br.com.erudio.file.exporter.factory;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import br.com.erudio.exception.BadRequestException;
import br.com.erudio.exception.handler.CustomEntityResponseHandler;
import br.com.erudio.file.exporter.MediaTypes;
import br.com.erudio.file.exporter.contract.FileExporter;
import br.com.erudio.file.exporter.impl.CsvExporter;
import br.com.erudio.file.exporter.impl.XlsxExporter;

@Component
public class FileExporterFactory {

    private final CustomEntityResponseHandler customEntityResponseHandler;

	private org.slf4j.Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);
	
	@Autowired
	private ApplicationContext context;

    FileExporterFactory(CustomEntityResponseHandler customEntityResponseHandler) {
        this.customEntityResponseHandler = customEntityResponseHandler;
    }
	
	public FileExporter getExporter(String acceptHeader) throws Exception{
		
		if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
			// return new XlsxImporter();
			return context.getBean(XlsxExporter.class);
		}else if(acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
			return context.getBean(CsvExporter.class);
		}else {
			throw new BadRequestException("Invalid File Format!");
		}
		
	}
	
}
