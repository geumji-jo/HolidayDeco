package com.hdd.hdeco.service;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.hdd.hdeco.domain.UserDTO;
import com.hdd.hdeco.mapper.UserMapper;
import com.hdd.hdeco.util.JavaMailUtil;
import com.hdd.hdeco.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	//field
	private final UserMapper userMapper;
	private final JavaMailUtil javaMailUtil;
	private final SecurityUtil securityUtil;

	@Override
	public void join(HttpServletRequest request, HttpServletResponse response) {

		// 요청 파라미터
		String url = request.getParameter("url"); // 로그인 화면의 이전 주소(로그인 후 되돌아갈 주소)
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");

		// 비밀번호 SHA-256 암호화
		//pw = securityUtil.getSha256(pw);

		// UserDTO 만들기
		UserDTO userDTO = new UserDTO();
		userDTO.setId(id);
		userDTO.setPw(pw);

		// DB에서 UserDTO 조회하기
		UserDTO loginUserDTO = userMapper.selectUserByUserDTO(userDTO);

		// ID, PW가 일치하는 회원이 있으면 로그인 성공
		// 1. session에 ID 저장하기
		// 2. 회원 접속 기록 남기기
		// 3. 이전 페이지로 이동하기
		if (loginUserDTO != null) {

			HttpSession session = request.getSession();
			session.setAttribute("loginId", id);
			session.setAttribute("adminChk", loginUserDTO.getAdminCheck()); // 구분 값 가져오기

			int updateResult = userMapper.updateUserAccess(id);
			if (updateResult == 0) {
				userMapper.insertUserAccess(id);
			}

			try {
				response.sendRedirect(url);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		// ID, PW가 일치하는 회원이 없으면 로그인 실패
		else {

			// 응답
			try {

				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('일치하는 회원 정보가 없습니다.');");
				out.println("location.href='" + request.getContextPath() + "/';");
				out.println("</script>");
				out.flush();
				out.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}


	}

	@Override
	public void login(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void autologin(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void leave(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void restore(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean checkPw(String id, String pw) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UserDTO getUserById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int updateMypage(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyUserInfo(UserDTO userDTO) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<String, Object> findUser(String name, String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findPw(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyPw(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Map<String, Object> verifyId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> verifyEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> sendAuthCode(String email) {
		// TODO Auto-generated method stub
		return null;
	}

}
