package com.piccologrupa.zamjenasoba.controllers;

import com.piccologrupa.zamjenasoba.ZamjenaHandler;
import com.piccologrupa.zamjenasoba.dao.DomRepository;
import com.piccologrupa.zamjenasoba.dao.FilterRepository;
import com.piccologrupa.zamjenasoba.dao.LajkaniOglasRepository;
import com.piccologrupa.zamjenasoba.dao.MogucaZamjenaRepository;
import com.piccologrupa.zamjenasoba.dao.OglasRepository;
import com.piccologrupa.zamjenasoba.dao.StudentRepository;
import com.piccologrupa.zamjenasoba.domain.*;
import com.piccologrupa.zamjenasoba.request.FilterRequest;
import com.piccologrupa.zamjenasoba.request.LikeRequest;
import com.piccologrupa.zamjenasoba.response.FilterResponse;
import com.piccologrupa.zamjenasoba.response.MessageResponse;
import com.piccologrupa.zamjenasoba.response.OglasResponse;
import com.piccologrupa.zamjenasoba.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/home")
public class HomepageController {

	@Autowired
	private OglasRepository oglasRepository;
	
	@Autowired
	private LajkaniOglasRepository lajkaniOglasRepository;
	
	@Autowired
	private FilterRepository filterRepository;
	
	@Autowired
	private DomRepository domRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private ZamjenaHandler zamjenaHandler;
	
	@Autowired
	private MogucaZamjenaRepository mogucaZamjenaRepository;

	//vraca sve oglase
	@GetMapping("/")
	public ResponseEntity<?> listAds() {

		Student student = AuthController.authorize();
		List<OglasResponse> rezultat = new ArrayList<>();
		if (student == null) {
			List<Oglas> oglasi = oglasRepository.findAll();

			for(Oglas o : oglasi){
				if(o.isAktivan()){
					rezultat.add(new OglasResponse(o, 0));
				}
			}
			return ResponseEntity.ok(rezultat);
		}else{

			if(student.getUserRole() != UserRole.STUDENT ){
				return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
			}

			List<Oglas> oglasi = oglasRepository.findAll();
			List<LajkaniOglas> lajkaniOglasi = lajkaniOglasRepository.findAll();
			for(Oglas o: oglasi){
				boolean lajkan = false;
				if(o.isAktivan()){
					for(LajkaniOglas l : lajkaniOglasi){
						if(o.getIdOglas() == l.getOglas().getIdOglas() && l.getStudent().getId() == student.getId()){
							if(l.getStupanjLajkanja() != 4){
								rezultat.add(new OglasResponse(o, l.getStupanjLajkanja()));
							}
							lajkan = true;
							break;
						}
					}
					if(!lajkan){
						rezultat.add(new OglasResponse(o,0));
					}
				}
			}
		}
		return ResponseEntity.ok(rezultat);
	}

	//vraca vlastite oglase
	@GetMapping("/vlastitiOglasi")
	public ResponseEntity<?> listPersonalAds() {

		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> filtriraniOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				filtriraniOglasi.add(o);
			}
		}
		return ResponseEntity.ok(filtriraniOglasi);
	}
	
	private static Oglas findActive(List<Oglas> oglasi) {
		Oglas oglas = null;
		for (Oglas o : oglasi) {
			if (o.isAktivan()) {
				oglas = o;
				break;
			}
		}
		return oglas;
	}
	
	@PostMapping("/")
	public ResponseEntity<?> likeAd(@Valid @RequestBody LikeRequest likeRequest) {
		//student koji je lajkao oglas
		Student student1 = AuthController.authorize();
		if (student1 == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student1.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student1.getId()) {
				studentoviOglasi.add(o);
			}
		}
		
		if (studentoviOglasi.size() == 0 || findActive(studentoviOglasi) == null) {
			return ResponseEntity.badRequest().body("Student nema nijedan aktivan oglas");
		}
		
		Oglas oglas = oglasRepository.findById(likeRequest.getOglasId()).get();
		int stupanjLajkanja = likeRequest.getStupanjLajkanja();
		
		Set<Student> lajkovi = studentRepository.findByLajkoviOglas(oglas);
		
		if (lajkovi.contains(student1)) {
			LajkaniOglasKey key = new LajkaniOglasKey(student1.getId(), oglas.getIdOglas());
			LajkaniOglas lajkaniOglas = lajkaniOglasRepository.findById(key).get();
			
			if (stupanjLajkanja == 0) {
				lajkaniOglasRepository.delete(lajkaniOglas);
				return ResponseEntity.ok(likeRequest);
				
			} else {
				lajkaniOglas.setStupanjLajkanja(stupanjLajkanja);
				lajkaniOglas.setProcitano(false);
				lajkaniOglasRepository.save(lajkaniOglas);
			}
			
		} else {
			LajkaniOglas lajkaniOglas = new LajkaniOglas(student1, oglas, stupanjLajkanja);
			lajkaniOglasRepository.save(lajkaniOglas);
		}
		
		Student student2 = oglas.getStudent();
		EmailServiceImpl emailService = new EmailServiceImpl();
		String emailText = "Poštovani, \n\nNetko je lajkao vaš oglas! Prijavite se u aplikaciju i provjerite nove obavijesti!";
		emailService.sendEmail(student2.getEmail(), "Obavijest", emailText);
		
		return ResponseEntity.ok(likeRequest);

	}
	
	public void sendSwapEmail(Student student1, Student student2) {
		EmailServiceImpl emailService = new EmailServiceImpl();
		String emailText = "Poštovani, \n\nkliknite na ovaj link ukoliko želite potvrditi zamjenu!\nhttps://room-switchr.herokuapp.com/PotvrdiZamjenu";
		emailService.sendEmail(student1.getEmail(), "Potvrda zamjene", emailText);
		emailService.sendEmail(student2.getEmail(), "Potvrda zamjene", emailText);
	}

//	@GetMapping("/sendSwapEmail")
//	public ResponseEntity<?> sendSwapEmail(){
//		Student student = AuthController.authorize();
//		if (student == null) {
//			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
//		}else if(student.getUserRole() != UserRole.STUDENT ){
//			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
//		}
//
//		EmailServiceImpl emailService = new EmailServiceImpl();
//		String emailText = "Poštovani, \n\nkliknite na ovaj link ukoliko želite potvrditi zamjenu!\nhttps://room-switchr.herokuapp.com/PotvrdiZamjenu";
//		emailService.sendEmail(student.getEmail(), "Potvrda zamjene", emailText);
//		return ResponseEntity.ok(new MessageResponse("Email uspješno poslan"));
//	}

//	private static final HashMap<Student, Oglas> potvrde = new HashMap<>();
//
//	@PostMapping("/confirmSwap")
//	public ResponseEntity<?> confirmSwap(@Valid @RequestBody Oglas oglas){
//		Student student = AuthController.authorize();
//		if (student == null) {
//			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
//		}else if(student.getUserRole() != UserRole.STUDENT ){
//			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
//		}
//
//		Student oglasStudent = oglas.getStudent();
//		if(potvrde.containsKey(oglasStudent)){
//			ZakljucaneZamjene zamjena = new ZakljucaneZamjene(oglas, potvrde.get(oglasStudent));
//			zakljucaneZamjeneRepository.save(zamjena);
//			potvrde.remove(oglasStudent);
//
//			EmailServiceImpl emailService = new EmailServiceImpl();
//			String text = "Poštovani, \nzamjena soba uspješno provedena!";
//			emailService.sendEmail(student.getEmail(), "Zamjena potvrđena", text);
//			emailService.sendEmail(oglasStudent.getEmail(), "Zamjena potvrđena", text);
//			//obavijesti sc buraza
//			return ResponseEntity.ok(new MessageResponse("Potvrda zamjene provedena do kraja"));
//		}else{
//			potvrde.put(student, oglas);
//			return ResponseEntity.ok(new MessageResponse("Potvrda zamjene zabiljezena"));
//		}
//	}
	
	//aktivni oglasi studenata koji su lajkali korisnikov oglas
	@GetMapping("/oglasiStudenataKojiSuLajkali")
	public ResponseEntity<?> listPosts(){

		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}

		//Oglas studentovOglas = oglasRepository.findActiveByStudent(student).get();
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				studentoviOglasi.add(o);
			}
		}
		
		if (studentoviOglasi.size() == 0 || findActive(studentoviOglasi) == null) {
			return ResponseEntity.ok(null);
		}
		
		Oglas studentovOglas = findActive(studentoviOglasi);
		
		Set<Student> studentiKojiSuLajkali = studentRepository.findByLajkoviOglas(studentovOglas);
		List<OglasResponse> oglasiStudenata = new ArrayList<>();
		
		for(Oglas o : oglasi) {
			if (studentiKojiSuLajkali.contains(o.getStudent())) {
				if (o.isAktivan()) {
					LajkaniOglasKey key = new LajkaniOglasKey(student.getId(), o.getIdOglas());
					Optional<LajkaniOglas> lajkaniOglas = lajkaniOglasRepository.findById(key);
					int stupanjLajkanja = 0;
					if (lajkaniOglas.isPresent()) {
						stupanjLajkanja = lajkaniOglas.get().getStupanjLajkanja();
					}
					oglasiStudenata.add(new OglasResponse(o, stupanjLajkanja));
				}
			}
		}
		
		return ResponseEntity.ok(oglasiStudenata);
	}
	
	@GetMapping("/zamjene")
	public ResponseEntity<?> getZamjene() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}
		else if (student.getUserRole() == UserRole.SC_DJELATNIK) {
			return ResponseEntity.ok(zamjenaHandler.getPotvrdjeniLanci());
		}
		else if (student.getUserRole() == UserRole.STUDENT) {
			
			List<Oglas> oglasi = oglasRepository.findAll();
			List<Oglas> studentoviOglasi = new ArrayList<>();
			for (Oglas o : oglasi) {
				if (o.getStudent().getId() == student.getId()) {
					studentoviOglasi.add(o);
				}
			}
			
			if (studentoviOglasi.size() == 0 || findActive(studentoviOglasi) == null) {
				return ResponseEntity.ok(null);
			}
			
			Oglas studentovOglas = findActive(studentoviOglasi);
			return ResponseEntity.ok(zamjenaHandler.getPonudjeneZamjene(studentovOglas));
			
		}
		else
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT ili SC_DJELATNIK uloga.");
	}

	
//	//aktivni oglasi studenata koji su lajkali korisnikov oglas
//	@GetMapping("/oglasiStudenataKojiSuLajkali")
//	public ResponseEntity<?> listPosts(){
//
//		Student student = AuthController.authorize();
//		if (student == null) {
//			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
//		}else if(student.getUserRole() != UserRole.STUDENT ){
//			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
//		}
//
//		Oglas studentovOglas = oglasRepository.findByStudent(student).get();
//
//		List<LajkaniOglas> lajkaniOglasi = lajkaniOglasRepository.findAll();
//		List<LajkaniOglas> lajkoviStudentovogOglasa = new ArrayList<>();
//
//		for(LajkaniOglas l : lajkaniOglasi){
//			if(l.getOglas().getIdOglas() == studentovOglas.getIdOglas()){
//				lajkoviStudentovogOglasa.add(l);
//			}
//		}
//
//		Set<Student> studentiKojiSuLajkali = new HashSet<>();
//		for(LajkaniOglas l : lajkoviStudentovogOglasa){
//			for(Student s : l.getStudentSet()){
//				studentiKojiSuLajkali.add(s);
//			}
//		}
//
//		List<Oglas> rezultat = new ArrayList<>();
//
//		for(Student s : studentiKojiSuLajkali){
//			rezultat.add(oglasRepository.findByStudent(s).get());
//		}
//
//		return ResponseEntity.ok(rezultat);
//	}
	
	@PostMapping("/filter")
	public ResponseEntity<?> saveFilter(@Valid @RequestBody FilterRequest filterRequest) {
		
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		if (filterRepository.findByStudent(student).isPresent()) 
			filterRepository.delete(filterRepository.findByStudent(student).get());
		
		Filter filter = new Filter(filterRequest.getDom(), student, filterRequest.getKat(), filterRequest.getKategorijaSobe(), filterRequest.getPaviljon());
		filterRepository.save(filter);
		
		return ResponseEntity.ok(new FilterResponse(filter.getDom(), filter.getKat(), filter.getKategorijaSobe(), filter.getPaviljon()));
		
	}
	
	@GetMapping("/filter")
	public ResponseEntity<?> getFilter() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		}else if(student.getUserRole() != UserRole.STUDENT ){
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		Optional<Filter> optFilter = filterRepository.findByStudent(student);
		if (optFilter.isEmpty()) {
			return ResponseEntity.badRequest().body("Ne popstoji filter za trenutnog studenta.");
		}
		
		Filter filter = optFilter.get();
		return ResponseEntity.ok(new FilterResponse(filter.getDom(), filter.getKat(), filter.getKategorijaSobe(), filter.getPaviljon()));
	}
	
	@GetMapping("/notification/like")
	public ResponseEntity<?> getLikeNotifications() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ) {
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				studentoviOglasi.add(o);
			}
		}
		Oglas oglas = findActive(studentoviOglasi);
		
		if(oglas != null) {
			List<LajkaniOglas> lajkaniOglasi = lajkaniOglasRepository.findByOglas(oglas);
			List<LajkaniOglas> neprocitaniOglasi = new ArrayList<>();
			for(LajkaniOglas o : lajkaniOglasi) {
				if (!o.isProcitano()) {
					neprocitaniOglasi.add(o);
				}
			}
			return ResponseEntity.ok(neprocitaniOglasi.size());
		} else {
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/notification/like")
	public ResponseEntity<?> removeLikeNotifications() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ) {
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				studentoviOglasi.add(o);
			}
		}
		Oglas oglas = findActive(studentoviOglasi);
		
		if(oglas != null) {
			List<LajkaniOglas> lajkaniOglasi = lajkaniOglasRepository.findByOglas(oglas);
			List<LajkaniOglas> neprocitaniOglasi = new ArrayList<>();
			for(LajkaniOglas o : lajkaniOglasi) {
				if (!o.isProcitano()) {
					neprocitaniOglasi.add(o);
					o.setProcitano(true);
					lajkaniOglasRepository.save(o);
				}
			}
			if (neprocitaniOglasi.size() != 0) 
				return ResponseEntity.ok(neprocitaniOglasi);
		} 
		return ResponseEntity.ok(new MessageResponse("Student novih lajkova."));
	}
	
	@GetMapping("/notification/swap")
	public ResponseEntity<?> getSwapNotifications() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ) {
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				studentoviOglasi.add(o);
			}
		}
		Oglas oglas = findActive(studentoviOglasi);
		
		if (oglas != null) {
			List<MogucaZamjena> moguceZamjene = zamjenaHandler.getPonudjeneZamjene(oglas);
			List<MogucaZamjena> neprocitaneZamjene = new ArrayList<>();
			for (MogucaZamjena mz : moguceZamjene) {
				if (!mz.isProcitano()) {
					neprocitaneZamjene.add(mz);
				}
			}
			return ResponseEntity.ok(neprocitaneZamjene.size());
		} else {
			return ResponseEntity.ok(0);
		}
	}
	
	@PostMapping("/notification/swap")
	public ResponseEntity<?> removeSwapNotifications() {
		Student student = AuthController.authorize();
		if (student == null) {
			return ResponseEntity.badRequest().body("Nije poslan token za autorizaciju");
		} else if(student.getUserRole() != UserRole.STUDENT ) {
			return ResponseEntity.badRequest().body("User nema autorizaciju za pristup ovoj metodi. Potrebna je STUDENT uloga.");
		}
		
		List<Oglas> oglasi = oglasRepository.findAll();
		List<Oglas> studentoviOglasi = new ArrayList<>();
		for (Oglas o : oglasi) {
			if (o.getStudent().getId() == student.getId()) {
				studentoviOglasi.add(o);
			}
		}
		Oglas oglas = findActive(studentoviOglasi);
		
		if (oglas != null) {
			List<MogucaZamjena> moguceZamjene = zamjenaHandler.getPonudjeneZamjene(oglas);
			List<MogucaZamjena> neprocitaneZamjene = new ArrayList<>();
			for (MogucaZamjena mz : moguceZamjene) {
				if (!mz.isProcitano()) {
					neprocitaneZamjene.add(mz);
					mz.setProcitano(true);
					mogucaZamjenaRepository.save(mz);
				}
			}
			if (neprocitaneZamjene.size() != 0)
				return ResponseEntity.ok(neprocitaneZamjene);
		} 
		return ResponseEntity.ok(new MessageResponse("Nema neprocitanih zamjena."));
	}
	
}
