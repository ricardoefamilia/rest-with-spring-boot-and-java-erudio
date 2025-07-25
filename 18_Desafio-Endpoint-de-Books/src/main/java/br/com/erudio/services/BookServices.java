package br.com.erudio.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.dto.BookDTO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;

import static br.com.erudio.mapper.ObjectMapper.parseListObjects;
import static br.com.erudio.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

	private Logger logger = LoggerFactory.getLogger(BookServices.class.getName());
	
	@Autowired
	BookRepository repository;
	
	public List<BookDTO> findAll(){
		logger.info("Finding all books!");
		var books = parseListObjects(repository.findAll(), BookDTO.class);
		books.forEach(this::addHateoasLinks);
		return books;
		
	}
	
	public BookDTO findById(Long id) {
		logger.info("Finding one Book!");
		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		var dto = parseObject(entity, BookDTO.class);
		addHateoasLinks(dto);
		return dto;
	}
	
	public BookDTO create(BookDTO book) {
		logger.info("Creating one Book!");
		if (book == null) throw new RequiredObjectIsNullException();
		var entity = parseObject(book, Book.class);
		var dto = parseObject(repository.save(entity), BookDTO.class);
		addHateoasLinks(dto);
		return dto;
	}
	
	public BookDTO update(BookDTO book) {
		logger.info("Updating one Book!");
		if (book == null) throw new RequiredObjectIsNullException();
		Book entity =  repository.findById(book.getId())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		
		var dto = parseObject(repository.save(entity), BookDTO.class);
		addHateoasLinks(dto);
		return dto;
	}
	
	public void delete(Long id) {
		logger.info("Deleting one Book!");
		
		Book entity =  repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		
		repository.delete(entity);
	}

	private void addHateoasLinks(BookDTO dto) {
		dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
		dto.add(linkTo(methodOn(BookController.class).findAll()).withRel("findAll").withType("GET"));
		dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
		dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
		dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
	}
}
