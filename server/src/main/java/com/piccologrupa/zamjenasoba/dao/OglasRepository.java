package com.piccologrupa.zamjenasoba.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.Oglas;
import com.piccologrupa.zamjenasoba.domain.Student;

@Repository
public interface OglasRepository extends JpaRepository<Oglas, Long> {

	List<Oglas> findByStudent(Student student);

	@Query("SELECT o FROM oglas o WHERE o.student = ?1 AND aktivan = true")
	Optional<Oglas> findActiveByStudent(Student student);

	Set<Oglas> findByLajkoviStudent(Student student);

}
