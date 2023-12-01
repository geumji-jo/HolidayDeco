package com.hdd.hdeco.intercept;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
     HttpSession session = request.getSession();
     Object loginUser = session.getAttribute("loginId");
         
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser != null && request.getRequestURI().endsWith("/login.html")) {
        response.sendRedirect("/");
        return false;
     }
     
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser != null && request.getRequestURI().endsWith("/join.html")) {
        response.sendRedirect("/");
        return false;
     }
     
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser != null && request.getRequestURI().endsWith("/agree.html")) {
        response.sendRedirect("/");
        return false;
     }
     
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser != null && request.getRequestURI().endsWith("/findId.html")) {
        response.sendRedirect("/");
        return false;
     }
     
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser != null && request.getRequestURI().endsWith("/findPw.html")) {
        response.sendRedirect("/");
        return false;
     }
     
     // 예: 로그인 상태가 확인되면 index 페이지로 리다이렉트
     if (loginUser == null && request.getRequestURI().endsWith("/mypageHome.html")) {
        response.sendRedirect("/");
        return false;
     }
     

     
     return true;     // 컨트롤러의 요청이 처리된다.
   
   
   }
}