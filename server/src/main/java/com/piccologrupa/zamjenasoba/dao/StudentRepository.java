package com.piccologrupa.zamjenasoba.dao;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.piccologrupa.zamjenasoba.domain.Oglas;
import com.piccologrupa.zamjenasoba.domain.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	  Optional<Student> findByUsername(String username);
	  
	  Boolean existsByUsername(String username);

	  Boolean existsByEmail(String email);
	  
	  Set<Student> findByLajkoviOglas(Oglas oglas);

}
