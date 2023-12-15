package com.hdd.hdeco.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.hdd.hdeco.service.UserService;

// 로그인 여부를 확인해서
// 로그인이 되어 있지 않으면 로그인 페이지로 이동시키는 인터셉터

@Component
public class PwCheckInterceptor implements HandlerInterceptor {
	
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     //checkPw 메서드를 호출하여 비밀번호 확인 로직을 수행
  	 HttpSession session = request.getSession();
  	 
     if (session.getAttribute("isCorrect") == null || session.getAttribute("isCorrect") == "false") {
       // 응답
    	 response.setContentType("text/html; charset=UTF-8");
    	 PrintWriter out = response.getWriter();
       out.println("<script>");
       out.println("alert('해당 기능은 사용할 수 없습니다.');");
       out.println("location.href='" + request.getContextPath() + "/user/checkPw.html';");
       out.println("</script>");
       out.flush();
       out.close();
       
       return false; // 요청을 처리하지 않고 중단합니다.
   } else {
  // isCorrect 세션 속성을 삭제
     session.removeAttribute("isCorrect");
   }

   return true; // 비밀번호가 일치하면 요청을 계속 진행합니다.
   }
}