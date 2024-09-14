package com.canal.controller;

import com.canal.domain.UserEntity;
import com.canal.dto.UserDto;
import com.canal.security.JwtUtil;
import com.canal.service.UserService;
import com.canal.vo.RequestJoin;
import com.canal.vo.RequestLogin;
import com.canal.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/members")
public class ExampleController {

	private final UserService userService;
	private final AuthenticationManager authManager;
	private final JwtUtil jwtUtil;

	@Autowired
	public ExampleController(UserService userService,AuthenticationManager authManager,JwtUtil jwtUtil) {
		this.userService = userService;
		this.authManager = authManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestLogin requestLogin) {
		try{
			// 인증 시도
			Authentication authentication = authManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestLogin.getUserId(),requestLogin.getUserPwd()));

			// JWT 생성
			String getUserId = authentication.getName();
			String token = jwtUtil.generateToken(getUserId);

			// 헤더에 토큰 넣기
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);

			return ResponseEntity.ok().headers(headers).body("로그인 성공");
		}catch (AuthenticationException e){
			return ResponseEntity.status(401).body("인증 실패");
		}
	}
	//	@PostMapping("/login")
//	public String login(@RequestBody RequestLogin user){
////		"redirect:http://frontend-domain.com/login"
//		return "redirect:/login";
//	}
	@PostMapping("/users")
	public ResponseEntity<ResponseUser> createUser(@RequestBody RequestJoin user){
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		UserDto userDto = modelMapper.map(user, UserDto.class);
		userService.createUser(userDto);

		ResponseUser responseUser = modelMapper.map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.CREATED).body(responseUser); // success: 201, 저장한 값
	}
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUser>> getAllUsers() {
		Iterable<UserEntity> userList = userService.getAllUsers();

		List<ResponseUser> resultList = new ArrayList<>();
		userList.forEach(value -> {
			resultList.add(new ModelMapper().map(value, ResponseUser.class));
		});

		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}

	@GetMapping("/users/{userSeq}")
	public ResponseEntity<ResponseUser> getUser(@PathVariable("userSeq") Long userSeq) {
		UserDto userDto = userService.getUserByUserSeq(userSeq);

		ResponseUser responseUser = new ModelMapper().map(userDto, ResponseUser.class);

		return ResponseEntity.status(HttpStatus.OK).body(responseUser);
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
