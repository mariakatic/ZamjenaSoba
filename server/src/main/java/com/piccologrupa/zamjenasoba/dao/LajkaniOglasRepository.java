package com.piccologrupa.zamjenasoba.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.LajkaniOglas;
import com.piccologrupa.zamjenasoba.domain.LajkaniOglasKey;
import com.piccologrupa.zamjenasoba.domain.Oglas;
import com.piccologrupa.zamjenasoba.domain.Student;

@Repository
public interface LajkaniOglasRepository extends JpaRepository<LajkaniOglas, LajkaniOglasKey> {
	
	Optional<LajkaniOglas> findByIdLajk(LajkaniOglasKey idLajk);
	
	List<LajkaniOglas> findByOglas(Oglas oglas);
	
	List<LajkaniOglas> findByStudent(Student student);
	
	boolean existsByOglas(Oglas oglas);

}
