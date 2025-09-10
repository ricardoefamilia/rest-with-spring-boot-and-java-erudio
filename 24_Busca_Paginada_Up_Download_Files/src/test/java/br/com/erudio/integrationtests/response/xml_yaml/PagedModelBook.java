package br.com.erudio.integrationtests.response.xml_yaml;

import java.io.Serializable;
import java.util.List;

import br.com.erudio.integrationtests.dto.BookDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelBook implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="content")
	public List<BookDTO> content;
	
	public PagedModelBook() {}

	public List<BookDTO> getContent() {
		return content;
	}

	public void setContent(List<BookDTO> content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
