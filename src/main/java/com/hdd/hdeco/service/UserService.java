package com.hdd.hdeco.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdd.hdeco.domain.UserDTO;

public interface UserService {

  public Map<String, Object> verifyId(String id);
  public Map<String, Object> verifyEmail(String email);
  public Map<String, Object> sendAuthCode(String email);
  public void join(HttpServletRequest request, HttpServletResponse response);
  public void login(HttpServletRequest request, HttpServletResponse response);
  public void autologin(HttpServletRequest request, HttpServletResponse response);
  public void logout(HttpServletRequest request, HttpServletResponse response);
  public void out(HttpServletRequest request, HttpServletResponse response);
//
  public void sleepUserHandle();
  public void restore(HttpServletRequest request, HttpServletResponse response);
  public boolean checkPw(HttpServletRequest request);
  public UserDTO getUserById(String id);
//  public int updateMypage(HttpServletRequest request, HttpServletResponse response);
//  public Map<String, Object> findUser(String name, String email);
  public Map<String, Object> findId(String name, String email);
  public Map<String, Object> findPw(UserDTO userDTO);
  public Map<String, Object> sendTempPw(UserDTO userDTO);
  public Map<String, Object> modifyPw(HttpServletRequest request);
  public Map<String, Object> modifyEmail(HttpServletRequest request);
  public Map<String, Object> modifyInfo(HttpServletRequest request);
 // public UserDTO verifyTemporaryPassword(String id, String temporaryPassword);  


  
  
  
  
  
}