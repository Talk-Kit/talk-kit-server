package com.canal.controller;

import com.canal.dto.RequestJoin;
import com.canal.dto.RequestLoginRecord;
import com.canal.dto.ResponseUsersRecord;
import com.canal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class ExampleController {

	private final UserService userService;

	@Autowired
	public ExampleController(UserService userService) { this.userService = userService; }

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestLoginRecord requestLoginRecord) {

		try {
			String token = userService.authenticateAndGenerateToken(requestLoginRecord);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);

			return ResponseEntity.ok().headers(headers).body("로그인 성공");
		} catch (AuthenticationException e) {
			return ResponseEntity.status(401).body("로그인 실패: 인증 실패");
		}

	}

	@PostMapping("/users")
	public String createUser(@RequestBody RequestJoin requestJoin){

		boolean success = userService.createUser(requestJoin);
		if (success){
			return "회원가입 성공";
		}else{
			return "회원가입 실패";
		}

	}

	@GetMapping("/users")
	public ResponseEntity<List<ResponseUsersRecord>> getAllUsers() {

		List<ResponseUsersRecord> resultList = userService.getAllUsers();

		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}

	@GetMapping("/users/{userSeq}")
	public ResponseEntity<ResponseUsersRecord> getUser(@PathVariable("userSeq") Long userSeq) {

		ResponseUsersRecord responseUsersRecord = userService.getUserByUserSeq(userSeq);

		return ResponseEntity.status(HttpStatus.OK).body(responseUsersRecord);
	}

	@GetMapping("/example")
	public String exampleController() {
		return "TEST-Get3";
	}

	@PostMapping("/example")
	public String examplePostController() {
		return "TEST-Post3";
	}
}
