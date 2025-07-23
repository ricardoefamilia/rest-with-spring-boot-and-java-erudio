package br.com.erudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.erudio.model.Book;

public interface BookRespository extends JpaRepository<Book, Long>{}
