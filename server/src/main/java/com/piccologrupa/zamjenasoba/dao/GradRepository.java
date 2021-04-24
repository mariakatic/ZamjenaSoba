package com.piccologrupa.zamjenasoba.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.Grad;

@Repository
public interface GradRepository extends JpaRepository<Grad, String> {

}
