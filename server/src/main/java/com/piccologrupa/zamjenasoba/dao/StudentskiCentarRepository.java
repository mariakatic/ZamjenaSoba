package com.piccologrupa.zamjenasoba.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.piccologrupa.zamjenasoba.domain.StudentskiCentar;

@Repository
public interface StudentskiCentarRepository extends JpaRepository<StudentskiCentar, Long> {

}
