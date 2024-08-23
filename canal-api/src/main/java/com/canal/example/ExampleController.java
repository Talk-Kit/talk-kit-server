package com.canal.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class ExampleController {

	@GetMapping("/example")
	public String exampleController() {
		return "TEST-Get3";
	}

	@PostMapping("/example")
	public String examplePostController() {
		return "TEST-Post3";
	}
}
