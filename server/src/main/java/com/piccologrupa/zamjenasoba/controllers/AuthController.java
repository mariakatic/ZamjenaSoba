package com.piccologrupa.zamjenasoba.controllers;

import com.piccologrupa.zamjenasoba.ZamjenaHandler;
import com.piccologrupa.zamjenasoba.dao.*;
import com.piccologrupa.zamjenasoba.domain.*;
import com.piccologrupa.zamjenasoba.jwt.JwtUtils;
import com.piccologrupa.zamjenasoba.request.ConfirmationRequest;
import com.piccologrupa.zamjenasoba.request.LoginRequest;
import com.piccologrupa.zamjenasoba.request.PostEditRequest;
import com.piccologrupa.zamjenasoba.request.SignupRequest;
import com.piccologrupa.zamjenasoba.response.JwtResponse;
import com.piccologrupa.zamjenasoba.response.MessageResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class AuthController {
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private SobaRepository sobaRepository;

	@Autowired
	private OglasRepository oglasRepository;

	@Autowired
	private DomRepository domRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private GradRepository gradRepository;
	
	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private ZamjenaHandler zamjenaHandler;

	public static Student authorize(){
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			return (Student) auth.getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		
		System.out.println(loginRequest.getUsername());
		System.out.println(loginRequest.getPassword());

		try{
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtUtils.generateJwtToken(authentication);

			Student userDetails = (Student) authentication.getPrincipal();
			UserRole role = userDetails.getUserRole();

			return ResponseEntity.ok(new JwtResponse(jwt,
													 userDetails.getId(),
													 userDetails.getUsername(),
													 userDetails.getEmail(),
													 role));

		}catch(BadCredentialsException exc){
			return ResponseEntity.badRequest().body(new MessageResponse("Bad credentials"));
		}
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (studentRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (studentRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		Student user = new Student(signUpRequest.getUsername(), 
								signUpRequest.getName(),
								signUpRequest.getJmbag(),
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		
		studentRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}

	@PostMapping("/post")
	public ResponseEntity<?> editPost(@Valid @RequestBody PostEditRequest postEditRequest) {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}

//		if (sobaRepository.findByIdStudenta(student).isEmpty()){
//			return ResponseEntity.badRequest().body("Student nema vlastitu sobu");
//		}
//		Soba soba = sobaRepository.findByIdStudenta(student).get();


		Dom dom;
		if (postEditRequest.getDomID() != null) {
			if (domRepository.findById(postEditRequest.getDomID()).isEmpty()){
				return ResponseEntity.badRequest().body("Dom nije pronadjen");
			}
			dom = domRepository.findById(postEditRequest.getDomID()).get();
		} else {
			dom = null;
		}

		Long postID = postEditRequest.getPostID();
		Oglas oglas;
		if (postID != null) {
			if (oglasRepository.findById(postID).isEmpty()){
				return ResponseEntity.badRequest().body("Zadani id oglasa nije pronadjen");
			}
			oglas = oglasRepository.findById(postID).get();
			if (oglas.getStudent().getId() != student.getId()) {
				return ResponseEntity.badRequest().body("Zadani oglas ne pripada prijavljenom korisniku");
			}
			oglas.setDom(dom);
			oglas.setPaviljon(postEditRequest.getPaviljon());
			oglas.setKat(postEditRequest.getKat());
			oglas.setKategorijaSobe(postEditRequest.getKategorijaSobe());
		} else {
			//postavi sve ostale studentove oglase da su neaktivni
			List<Oglas> oglasi = oglasRepository.findAll();
			List<Oglas> studentoviOglasi = new ArrayList<>();
			for(Oglas o : oglasi){
				if(o.getStudent().getId() == student.getId()){
					studentoviOglasi.add(o);
				}
			}

			for(Oglas o : studentoviOglasi){
				o.setAktivan(false);
				oglasRepository.save(o);
			}

			oglas = new Oglas(student, dom,
					postEditRequest.getPaviljon(),
					postEditRequest.getKat(),
					postEditRequest.getKategorijaSobe());
		}

		oglasRepository.save(oglas);

		return ResponseEntity.ok().body("Oglas uspjesno dodan");
	}

	@GetMapping("/zamjene")
	public ResponseEntity<?> getZamjene() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}
		else if (student.getUserRole() == UserRole.STUDENT) {
			Optional<Oglas> oglas = oglasRepository.findActiveByStudent(student);
			if (oglas.isEmpty())
				return ResponseEntity.badRequest().body("Ulogirani student nije postavio oglas");
			else
				return ResponseEntity.ok(zamjenaHandler.getPonudjeneZamjeneSaPotvrdom(oglas.get()));
		}
		else if (student.getUserRole() == UserRole.SC_DJELATNIK) {
			List<MogucaZamjena> zamjene = new ArrayList<>();
			for (Long lanac : zamjenaHandler.getPotvrdjeniLanci()) {
				zamjene.addAll(zamjenaHandler.getLanac(lanac));
			}
			return ResponseEntity.ok(zamjene);
		}
		else
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT ili SC_DJELATNIK uloga.");
	}

	@GetMapping("/lanac/{id}")
	public ResponseEntity<?> getLanac(@PathVariable("id")Long idLanca) {
		return ResponseEntity.ok(zamjenaHandler.getLanac(idLanca));
	}

	@PostMapping("/potvrdiZamjenu")
	public ResponseEntity<?> confirm(@Valid @RequestBody ConfirmationRequest confirmationRequest) {
		Student student = AuthController.authorize();
		Long idLanca = confirmationRequest.getIdLanca();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() == UserRole.STUDENT ) {

			Optional<Oglas> oglas = oglasRepository.findActiveByStudent(student);
			if (oglas.isEmpty()) {
				return ResponseEntity.badRequest().body("Student ne moze potvrditi ovu zamjenu");
			}
			if (!zamjenaHandler.getOglasiLanca(idLanca).contains(oglas.get().getIdOglas())) {
				return ResponseEntity.badRequest().body("Student ne moze potvrditi ovu zamjenu");
			}
			if (student.getPotvrdjeniLanac() != null) {
				if (zamjenaHandler.getBrojPotvrda(student.getPotvrdjeniLanac()) == zamjenaHandler.getLanacSize(student.getPotvrdjeniLanac()))
					return ResponseEntity.badRequest().body("Student vec ima zakljucanu potvrdu");
			}
			student.setPotvrdjeniLanac(idLanca);
			studentRepository.save(student);
			return ResponseEntity.ok("Zamjena potvrdjena");

		} else if (student.getUserRole() == UserRole.SC_DJELATNIK) {

			if (zamjenaHandler.getBrojPotvrda(idLanca) != zamjenaHandler.getLanacSize(idLanca))
				return ResponseEntity.badRequest().body("Zamjenu nisu potvrdili svi studenti");
			for (MogucaZamjena zamjena : zamjenaHandler.getLanac(idLanca)) {
				Oglas oglas = zamjena.getOglas();
				oglas.setAktivan(false);
				oglasRepository.save(oglas);
			}
			zamjenaHandler.brisiLanac(idLanca);
			return ResponseEntity.ok("Zamjena potvrdjena");

		} else
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT ili SC_DJELATNIK uloga.");
	}

	@PostMapping("/odbijZamjenu")
	public ResponseEntity<?> reject(@Valid @RequestBody ConfirmationRequest confirmationRequest) {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		Long idLanca = confirmationRequest.getIdLanca();
		Optional<Oglas> oglas = oglasRepository.findActiveByStudent(student);
		if (oglas.isEmpty()) {
			return ResponseEntity.badRequest().body("Student ne moze odbiti ovu zamjenu");
		}
		if (!zamjenaHandler.getOglasiLanca(idLanca).contains(oglas.get().getIdOglas())) {
			return ResponseEntity.badRequest().body("Student ne moze odbiti ovu zamjenu");
		}
		if (zamjenaHandler.getBrojPotvrda(idLanca) == zamjenaHandler.getLanacSize(idLanca)) {
			return ResponseEntity.badRequest().body("Zamjenu vise nije moguce odbiti");
		}
		zamjenaHandler.brisiLanac(idLanca);
		return ResponseEntity.ok("Zamjena potvrdjena");
	}

	@GetMapping("/grad")
	public ResponseEntity<?> getCities(){
		return ResponseEntity.ok(gradRepository.findAll());
	}

	@GetMapping("/dom")
	public ResponseEntity<?> getDom(){
		return ResponseEntity.ok(domRepository.findAll());
	}

	@GetMapping("/oglas/{id}")
	public ResponseEntity<?> getOglas(@PathVariable("id")Long id){
		return ResponseEntity.ok(oglasRepository.findById(id));
	}

	@GetMapping("/currUserRoom")
	public ResponseEntity<?> getCurrentUsersRoom(){
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}

		Optional<Soba> soba = sobaRepository.findByIdStudenta(student);

		if(soba.isPresent()){
			return ResponseEntity.ok(soba.get());
		}else{
			return ResponseEntity.badRequest().body(new MessageResponse("Student nema definiranu svoju sobu"));
		}

	}

}
