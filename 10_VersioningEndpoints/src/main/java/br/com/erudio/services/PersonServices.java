package br.com.erudio.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.data.dto.v1.PersonDTO;
import br.com.erudio.data.dto.v2.PersonDTOv2;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.mapper.custom.PersonMapper;

import static br.com.erudio.mapper.ObjectMapper.parseListObjects;
import static br.com.erudio.mapper.ObjectMapper.parseObject;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;

@Service
public class PersonServices {

	private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	@Autowired
	PersonMapper converter;
	
	public List<PersonDTO> findAll() {
		logger.info("Finding all people!");
		
		return parseListObjects(repository.findAll(), PersonDTO.class);
	}
	
	public PersonDTO findById(Long id) {
		logger.info("Finding one Person!");
		
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		return parseObject(entity, PersonDTO.class);
	}
	
	public PersonDTO create(PersonDTO person) {
		logger.info("Creating one Person!");
		var entity = parseObject(person, Person.class);
		return parseObject(repository.save(entity), PersonDTO.class);
	}
	
	public PersonDTOv2 createV2(PersonDTOv2 person) {
		logger.info("Creating one Person!");
		var entity = converter.convertDTOtoEntitty(person);
		return converter.convertEntittyToDTO(repository.save(entity));
	}
	
	public PersonDTO update(PersonDTO person) {
		logger.info("Updating one Person!");
		Person entity =  repository.findById(person.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return parseObject(repository.save(entity), PersonDTO.class);
	}
	
	public void delete(Long id) {
		logger.info("Deleting one Person!");
		
		Person entity =  repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}
	
}
