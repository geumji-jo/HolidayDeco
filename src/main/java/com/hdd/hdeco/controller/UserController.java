package com.hdd.hdeco.controller;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.UserDTO;
import com.hdd.hdeco.service.UserService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

  //field
  private final UserService userService;
    

  // 회원가입 - 이용약관
  @GetMapping("/agree.html")
  public String agreeForm() {
    return "user/agree";
  }
  
  // 회원가입화면
  @GetMapping("/join.html")
  public String joinForm(@RequestParam(value="location", required=false) String location  // 파라미터 location이 전달되지 않으면 빈 문자열("")이 String location에 저장된다.
                       , @RequestParam(value="event", required=false) String event        // 파라미터 event가 전달되지 않으면 빈 문자열("")이 String event에 저장된다.
                       , Model model) {
    model.addAttribute("location", location);
    model.addAttribute("event", event);
    return "user/join";
  }
  
  // 회원가입구현
  @PostMapping("/join.do")
  public void join(HttpServletRequest request, HttpServletResponse response) {
  userService.join(request, response);
  }

  
  // 회원가입 - 아이디 검사
  @ResponseBody
  @GetMapping(value="/verifyId.do", produces="application/json")
  public Map<String, Object> verifyId(@RequestParam("id") String id) {
    return userService.verifyId(id);
  }
 
  // 회원가입 - 이메일 검사
  @ResponseBody
  @GetMapping(value="/verifyEmail.do", produces="application/json")
  public Map<String, Object> verifyEmail(@RequestParam("email") String email) {
    return userService.verifyEmail(email);
  }
  
  // 회원가입 - 이메일 인증 코드
  @ResponseBody
  @GetMapping(value="/sendAuthCode.do", produces="application/json")
  public Map<String, Object> sendAuthCode(@RequestParam("email") String email) {
    return userService.sendAuthCode(email);
  }
  
  // 로그인화면
  @GetMapping("/login.html")
  public String loginForm(@RequestHeader("referer") String url, Model model) {
  	// 요청 헤더 referer : 로그인 화면으로 이동하기 직전의 주소를 저장하는 헤더 값
  	model.addAttribute("url", url);
  	return "user/login";
  }
  
  // 로그인구현
  @PostMapping("/login.do")
  public void login(HttpServletRequest request, HttpServletResponse response) {
  	userService.login(request, response);
  }
  
  // 로그아웃
  @GetMapping("/logout.do")
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    // 로그인이 되어 있는지 확인
    userService.logout(request, response);
    return "redirect:/";
  }
  
  // 휴면회원화면
  @GetMapping("/wakeup.html")
  public String wakeupForm() {
  	return "user/wakeup";
  }
  
  //휴면회원 - 일반회원으로 전환 구현
  @GetMapping("/restore.do")
  public void restore(HttpServletRequest request, HttpServletResponse response) {
  	userService.restore(request, response);
  }
  
  //아이디 찾기
  @GetMapping("/findId.html")
  public String findIdForm() {
  	return "user/findId";
  }
  
  //아이디 찾기 구현
  @ResponseBody
  @PostMapping(value="/findId.do", produces="application/json")  // 아이디 찾기
  public Map<String, Object> findId(@RequestParam("name") String name, @RequestParam("email") String email) {
    return userService.findId(name , email);
  }
  
  //비밀번호찾기 화면
	@GetMapping("/findPw.html")  // 비밀번호 찾기 화면으로 이동
	public String findPwForm() {
   return "user/findPw";
 }
	
	//비밀번호 찾기
  @ResponseBody
  @PostMapping(value="/findPw.do", produces="application/json")
  public Map<String, Object> findPw(@RequestBody UserDTO userDTO) {
    return userService.findPw(userDTO);
  }
  
	//비밀번호찾기 - 임시비밀번호 발급 및 전송
  @ResponseBody
  @PostMapping(value="/sendTempPw.do", produces="application/json")  
  public Map<String, Object> sendTempPw(@RequestBody UserDTO userDTO) {
    return userService.sendTempPw(userDTO);
  }
  
  //마이페이지메인홈
  @GetMapping("/myPageHome.html")
  public String mypage(HttpSession session, Model model) {  // 마이페이지로 이동
  	String id = (String) session.getAttribute("loginId");
  	model.addAttribute("loginUser", userService.getUserById(id));
  	return "user/myPageHome";
  }
  
  //개인정보수정화면 접근 직전 비밀번호 확인 화면으로 이동
  @GetMapping("/checkPw.html")  
  public String checkPwForm() {
    return "user/checkPw";
  }
  
  //개인정보수정화면 접근 패스워드 체크 구현
  @ResponseBody
  @PostMapping(value="/checkPw.do", produces="application/json")  // 사용자가 입력한 비밀번호가 맞는지 확인
  public Map<String, Object> checkPw(HttpServletRequest request) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("isCorrect", userService.checkPw(request));
    return map;
  }
  
  //개인정보수정 화면
  @GetMapping("/modifyInfo.html")
  public String modifyInfoForm(HttpSession session, Model model) {
  	// 로그인이 되어 있는지 확인
    if (session.getAttribute("loginId") == null) {
    // 로그인되어 있지 않으면 로그인 페이지로 리다이렉트
    return "redirect:/user/login.html";
  }
    // 세션에서 사용자의 ID를 가져옴
    String id = (String) session.getAttribute("loginId");
    // 서비스를 통해 사용자 정보를 가져옴
    model.addAttribute("loginUser", userService.getUserById(id));
	  return "user/modifyInfo";
	}
  
  //비밀번호 변경
  @ResponseBody
  @PostMapping(value="/modifyPw.do", produces="application/json")
  public Map<String, Object> modifyPw(HttpServletRequest request) {
    return userService.modifyPw(request);
  }
  
  //이메일 변경  
  @ResponseBody
  @PostMapping(value="/modifyEmail.do", produces="application/json")  
  public Map<String, Object> modifyEmail(HttpServletRequest request) {
    return userService.modifyEmail(request);
  }
  
  //개인정보수정 구현
  @ResponseBody
  @PostMapping(value="/modifyInfo.do", produces="application/json")  
  public Map<String, Object> modifyInfo(HttpServletRequest request){
    return userService.modifyInfo(request);    
  }
	
  
	//회원 탈퇴 화면
  @GetMapping("/out.html")
  public String outForm(HttpServletRequest request, HttpServletResponse response) {
   return "user/out";
  }
  
  // 회원 탈퇴
  @GetMapping("/out.do")
  public void out(HttpServletRequest request, HttpServletResponse response) {
    userService.out(request, response);
  }
  
	

  
	  
	 
	 
	 
	 
	//실험페이지
	  @GetMapping("/myPageHome2.html")
	  public String mypage1111(HttpSession session, Model model) {  // 마이페이지로 이동
	  	String id = (String) session.getAttribute("loginId");
	  	model.addAttribute("loginUser", userService.getUserById(id));
	  	return "user/myPageHome2";
	  }
}
	