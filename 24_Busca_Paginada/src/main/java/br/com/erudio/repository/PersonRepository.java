package br.com.erudio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.erudio.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long>{
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Person p SET p.enabled = false where p.id = :id")
	void disablePerson(@Param("id") Long id);
}
