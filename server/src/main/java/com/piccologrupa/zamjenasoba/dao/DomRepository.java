package com.piccologrupa.zamjenasoba.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.Dom;

@Repository
public interface DomRepository extends JpaRepository<Dom, Long> {
	
	Optional<Dom> findByIdDom(Long idDom);

}
