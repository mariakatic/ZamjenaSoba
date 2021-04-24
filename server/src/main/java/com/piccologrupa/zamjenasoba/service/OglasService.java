package com.piccologrupa.zamjenasoba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.piccologrupa.zamjenasoba.dao.OglasRepository;
import com.piccologrupa.zamjenasoba.domain.Student;

@Service
public class OglasService {
	
	@Autowired
	private OglasRepository oglasRepo;
	
	public void listAllAds() {
		
	}
	
	public void listFilteredAds(Student student) {
		
	}

}
