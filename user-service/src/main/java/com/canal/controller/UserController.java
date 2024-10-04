package com.canal.controller;

import com.canal.dto.*;
import com.canal.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-service")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Controller",description = "사용자 관련 전반적인 기능을 위한 컨트롤러입니다")
public class UserController {

	private final UserService userService;
	
	@Operation(summary = "로그인 API", description = "계정이 있는 사용자의 로그인을 진행합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 로그인 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 로그인 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestLoginRecord requestLoginRecord) {

		try {
			String token = userService.authenticateAndGenerateToken(requestLoginRecord);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + token);

			return ResponseEntity.ok().headers(headers).body("로그인 성공");
		} catch (Exception e) {
			return ResponseEntity.status(401).body("로그인 실패: 인증 실패");
		}

	}

	@Operation(summary = "이메일 존재 확인 API", description = "가입하려는 이메일이 존재하는지 확인합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK: 이메일 조회 성공"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/emailCheck")
	public ResponseEntity<?> existUserEmail(@RequestBody RequestMailCheck requestMailCheck) {
		return userService.existUserEmail(requestMailCheck);
	}

	@Operation(summary = "이메일 인증 API", description = "이메일 인증을 위한 인증 번호를 발송합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 인증 번호 발송 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 인증 번호 발송 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/emailConfirm")
	public ResponseEntity<?> sendEmail(@RequestBody RequestMailCheck requestMailCheck) {
		return userService.sendEmail(requestMailCheck);
	}

	@Operation(summary = "이메일 인증 번호 확인 API", description = "사용자가 입력한 이메일로 발송된 인증 번호를 확인합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK: 인증 번호 확인 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 인증 번호 확인 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/emailCodeConfirm")
	public ResponseEntity<?> sendEmailPath(@RequestBody RequestMailCodeCheck requestMailCodeCheck) {
		return userService.verifyEmailCode(requestMailCodeCheck);
	}

	@Operation(summary = "아이디 존재 확인 API", description = "가입하려는 아이디가 존재하는지 확인합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 아이디 조회 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 아이디 조회 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/userIdCheck")
	public ResponseEntity<?> existUserId(@RequestBody RequestUserIdCheck requestUserIdCheck) {
		return userService.existUserId(requestUserIdCheck);
	}

	@Operation(summary = "닉네임 존재 확인 API", description = "가입하려는 닉네임이 존재하는지 확인합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK: 닉네임 조회 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 닉네임 조회 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/userNicknameCheck")
	public ResponseEntity<?> existUserNickname(@RequestBody RequestUserNicknameCheck requestUserNicknameCheck) {
		return userService.existUserNickname(requestUserNicknameCheck);
	}

	@Operation(summary = "회원가입 API", description = "사용자의 정보를 등록합니다", security = {})
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 회원가입 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 회원가입 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@PostMapping("/users")
	public String createUser(@RequestBody RequestJoin requestJoin){

		boolean success = userService.createUser(requestJoin);
		if (success){
			return "회원가입 성공";
		}else{
			return "회원가입 실패";
		}

	}

	@Operation(summary = "회원 조회 API", description = "등록된 모든 회원들의 정보를 조회합니다")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 회원 정보 조회 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 회원 정보 조회 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@GetMapping("/users")
	public ResponseEntity<List<ResponseUsersRecord>> getAllUsers() {

		List<ResponseUsersRecord> resultList = userService.getAllUsers();

		return ResponseEntity.status(HttpStatus.OK).body(resultList);
	}

	@Operation(summary = "특정 회원 정보 조회 API", description = "특정 사용자의 정보를 조회합니다")
	@ApiResponses({
			@ApiResponse(responseCode = "201", description = "CREATED: 회원 정보 조회 성공"),
			@ApiResponse(responseCode = "400", description = "BAD REQUEST: 회원 정보 조회 실패. 요청값 확인 필요합니다"),
			@ApiResponse(responseCode = "401", description = "Unauthorized: 인증 실패. 주로 JWT 에러"),
			@ApiResponse(responseCode = "403", description = "Forbidden: 권한이 없는 페이지. 주로 잘못된 URL"),
			@ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR : 서버 다운 또는 로딩중"),
	})
	@GetMapping("/user")
	public ResponseEntity<ResponseUsersRecord> getUser(HttpServletRequest request) {

		ResponseUsersRecord responseUsersRecord = userService.getUserByJwt(request);

		return ResponseEntity.status(HttpStatus.OK).body(responseUsersRecord);
	}

	/*feign client*/
	@Operation(hidden = true)
	@GetMapping("/client/user")
	public ResponseEntity<Long> getUserSeq(@RequestHeader("Authorization") String auth){
		Long userSeq = userService.getUserSeq(auth);
		return ResponseEntity.status(HttpStatus.OK).body(userSeq);
	}

}
