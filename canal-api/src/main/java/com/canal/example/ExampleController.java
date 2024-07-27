package com.canal.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

	@GetMapping("/example")
	public void exampleController() {
		System.out.println("TEST");
	}

	@PostMapping("/example")
	public void example2Controller() {
		System.out.println("TEST");
	}
}
