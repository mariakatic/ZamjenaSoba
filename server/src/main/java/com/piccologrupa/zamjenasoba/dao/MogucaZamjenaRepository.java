package com.piccologrupa.zamjenasoba.dao;

import com.piccologrupa.zamjenasoba.domain.MogucaZamjena;
import com.piccologrupa.zamjenasoba.domain.Oglas;
import com.piccologrupa.zamjenasoba.domain.ZamjenaID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface MogucaZamjenaRepository extends JpaRepository<MogucaZamjena, ZamjenaID> {

    List<MogucaZamjena> findByIdLanca(Long idLanca);

    List<MogucaZamjena> findByOglas(Oglas oglas);

    @Transactional
    Long deleteByIdLanca(Long idLanca);

    @Query("SELECT DISTINCT idLanca FROM mogucaZamjena")
    List<Long> findAllIdLanca();
}
