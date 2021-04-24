package com.piccologrupa.zamjenasoba.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.Menza;

@Repository
public interface MenzaRepository extends JpaRepository<Menza, String> {

}
