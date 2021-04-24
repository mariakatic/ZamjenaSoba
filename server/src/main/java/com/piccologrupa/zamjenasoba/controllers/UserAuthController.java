package com.piccologrupa.zamjenasoba.controllers;

import java.util.ArrayList;
import java.util.List;

import com.piccologrupa.zamjenasoba.dao.StudentRepository;
import com.piccologrupa.zamjenasoba.domain.UserRole;
import com.piccologrupa.zamjenasoba.request.LoginRequest;
import com.piccologrupa.zamjenasoba.request.SignupRequest;
import com.piccologrupa.zamjenasoba.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.piccologrupa.zamjenasoba.domain.Student;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class UserAuthController {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private StudentRepository studentRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

	@GetMapping("/currentuser")
	public ResponseEntity<?> userAccess() {
		Student student = AuthController.authorize();
		if(student == null){
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}
		return ResponseEntity.ok(student);
	}

	@PostMapping("/currentuser")
	public ResponseEntity<?> changeUser(@Valid @RequestBody SignupRequest signUpRequest){

		Student student = AuthController.authorize();
		if(student == null){
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}

		student.setEmail(signUpRequest.getEmail());
		student.setJmbag(signUpRequest.getJmbag());
		student.setName(signUpRequest.getName());
		student.setUsername(signUpRequest.getUsername());
		//student.setPassword(encoder.encode(signUpRequest.getPassword()));

		studentRepository.save(student);
		return ResponseEntity.ok("User uspjesno azuriran");

	}

	@PostMapping("/changePassword")
	public ResponseEntity<?> changePass(@Valid @RequestBody LoginRequest loginRequest){

		Student student = AuthController.authorize();
		if(student == null){
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}

		student.setPassword(encoder.encode(loginRequest.getPassword()));
		studentRepository.save(student);
		return ResponseEntity.ok(new MessageResponse("Password uspjesno promijenjen"));
	}
}
