package com.piccologrupa.zamjenasoba.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.piccologrupa.zamjenasoba.domain.Filter;
import com.piccologrupa.zamjenasoba.domain.Student;

public interface FilterRepository extends JpaRepository<Filter, Long> {
	
	Optional<Filter> findByIdFilter(Long idFilter);
	
	Optional<Filter> findByStudent(Student student);

}
