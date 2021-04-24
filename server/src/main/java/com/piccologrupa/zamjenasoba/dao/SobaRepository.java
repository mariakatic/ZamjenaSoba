package com.piccologrupa.zamjenasoba.dao;

import com.piccologrupa.zamjenasoba.domain.Soba;
import com.piccologrupa.zamjenasoba.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SobaRepository extends JpaRepository<Soba, Long> {

    Optional<Soba> findByIdStudenta(Student idStudenta);

}
