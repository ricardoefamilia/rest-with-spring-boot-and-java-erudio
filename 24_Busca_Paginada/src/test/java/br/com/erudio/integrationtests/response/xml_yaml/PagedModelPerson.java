package br.com.erudio.integrationtests.response.xml_yaml;

import java.io.Serializable;
import java.util.List;

import br.com.erudio.integrationtests.dto.PersonDTO;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PagedModelPerson implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XmlElement(name="content")
	public List<PersonDTO> content;
	
	public PagedModelPerson() {}

	public List<PersonDTO> getContent() {
		return content;
	}

	public void setContent(List<PersonDTO> content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
