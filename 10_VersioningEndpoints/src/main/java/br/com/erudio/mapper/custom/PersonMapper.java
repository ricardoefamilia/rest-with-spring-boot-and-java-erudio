package br.com.erudio.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.erudio.data.dto.v2.PersonDTOv2;
import br.com.erudio.model.Person;

@Service
public class PersonMapper {

	public PersonDTOv2 convertEntittyToDTO(Person person) {
		PersonDTOv2 dto = new PersonDTOv2();
		dto.setId(person.getId());
		dto.setFirstName(person.getFirstName());
		dto.setLastName(person.getLastName());
		dto.setBirthDay(new Date());
		dto.setAddress(person.getAddress());
		dto.setGender(person.getGender());
		return dto;
	}
	
	public Person convertDTOtoEntitty(PersonDTOv2 person) {
		Person entity = new Person();
		entity.setId(person.getId());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
//		entity.setBirthDay(person.getBirthDay());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		return entity;
	}
}
