package com.piccologrupa.zamjenasoba.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class TestController1 {

	@GetMapping("/")
	public String hello() {
		return "index.html";
	}

}
