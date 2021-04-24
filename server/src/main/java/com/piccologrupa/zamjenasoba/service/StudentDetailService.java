package com.piccologrupa.zamjenasoba.service;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import com.piccologrupa.zamjenasoba.dao.StudentRepository;
import com.piccologrupa.zamjenasoba.domain.Student;

@Service
public class StudentDetailService implements UserDetailsService {
	
	@Autowired
	private final StudentRepository studentRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;

	public StudentDetailService(StudentRepository studentRepo) {
		super();
		this.studentRepository = studentRepo;
	}

	public boolean checkIfUserExists(String username) {
		Assert.notNull(username, "Username must be given");

		return studentRepository.findByUsername(username).isPresent();
	}
	
	public Student findById(Long id) {
		Assert.notNull(id, "Id must be given");
		
		return studentRepository.findById(id).get();
	}

	public Student findByUsername(String username) {
		Assert.notNull(username, "Username must be given");

		if (studentRepository.findByUsername(username).isPresent())
			return studentRepository.findByUsername(username).get();
		else
			throw new UsernameNotFoundException(MessageFormat.format("User with username {0} cannot be found.", username));
	}

	public Student registerStudent(Student student) {
		final String encryptedPwd = encoder.encode(student.getPassword());
		student.setPassword(encryptedPwd);
		return studentRepository.save(student);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (studentRepository.findByUsername(username).isPresent())
			return studentRepository.findByUsername(username).get();
	    else
	    	throw new UsernameNotFoundException(MessageFormat.format("User with username {0} cannot be found.", username));
	}

}
