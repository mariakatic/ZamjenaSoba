package com.piccologrupa.zamjenasoba.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.piccologrupa.zamjenasoba.domain.UserRole;
import com.piccologrupa.zamjenasoba.request.LoginRequest;
import com.piccologrupa.zamjenasoba.request.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testSuccessfulRegistration() throws Exception{

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@email.com");
		signupRequest.setUsername("testUser");
		signupRequest.setPassword("testPassword");
		signupRequest.setJmbag("0123456789");
		signupRequest.setName("TestniUser");
		signupRequest.setRole(UserRole.STUDENT);

		String json = new ObjectMapper().writeValueAsString(signupRequest);

		this.mockMvc.perform(post("/api/v1/signup").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("message").value("User registered successfully!"));

	}

	@Test
	public void testUnsuccessfulRegistrationUsernameTaken() throws Exception{

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test2@email.com");
		signupRequest.setUsername("testUser");
		signupRequest.setPassword("testPassword");
		signupRequest.setJmbag("0123456789");
		signupRequest.setName("TestniUser");
		signupRequest.setRole(UserRole.STUDENT);

		String json = new ObjectMapper().writeValueAsString(signupRequest);

		this.mockMvc.perform(post("/api/v1/signup").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("Error: Username is already taken!"));

	}

	@Test
	public void testUnsuccessfulRegistrationEmailTaken() throws Exception{

		SignupRequest signupRequest = new SignupRequest();
		signupRequest.setEmail("test@email.com");
		signupRequest.setUsername("testUser2");
		signupRequest.setPassword("testPassword");
		signupRequest.setJmbag("0123456789");
		signupRequest.setName("TestniUser");
		signupRequest.setRole(UserRole.STUDENT);

		String json = new ObjectMapper().writeValueAsString(signupRequest);

		this.mockMvc.perform(post("/api/v1/signup").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("Error: Email is already in use!"));

	}

	@Test
	public void testGetCurrentUserFromAuthToken() throws Exception{
		this.mockMvc.perform(get("/api/v1/currentuser").header("Authorization",
				"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0VXNlciIsImlhdCI6MTYxMDQ2ODk5OSwiZXhwIjoxNjEwNTU1Mzk5fQ.WxCCkmsZzUFj_RBHaPzI1h6C67daGaBkE6auADDZvIlQRCvOMI-hnLJdsNzVVQpAO65qLk9d_NmocMzj05G89Q"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("username").value("testUser"));
	}

	@Test
	public void testAuthTokenNotSent() throws Exception{
		this.mockMvc.perform(get("/api/v1/currentuser"))
				.andDo(print())
				.andExpect(status().is4xxClientError())
				.andExpect(content().string(containsString("Nije poslan token za autorizaciju")));
	}

	@Test
	public void testSuccessfulLogin() throws Exception{

		LoginRequest login = new LoginRequest();
		login.setUsername("testUser");
		login.setPassword("testPassword");

		String json = new ObjectMapper().writeValueAsString(login);

		this.mockMvc.perform(post("/api/v1/signin").contentType(MediaType.APPLICATION_JSON).content(json))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("username").value("testUser"));
	}

	@Test
	public void testUnsuccessfulLogin() throws Exception{

		LoginRequest login = new LoginRequest();
		login.setUsername("testUser");
		login.setPassword("wrongPassword");

		String json = new ObjectMapper().writeValueAsString(login);

		this.mockMvc.perform(post("/api/v1/signin").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("message").value("Bad credentials"));
	}

}
