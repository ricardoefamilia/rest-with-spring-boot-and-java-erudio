package br.com.erudio.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.erudio.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{
	
	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE Person p SET p.enabled = false where p.id = :id")
	void disablePerson(@Param("id") Long id);
	
	@Query("SELECT p FROM Person p where p.firstName LIKE LOWER(CONCAT('%',:firstName,'%'))")
	Page<Person> findPeopleByName(@Param("firstName") String firstName,Pageable pageable);
}
