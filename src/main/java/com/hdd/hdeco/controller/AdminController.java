package com.hdd.hdeco.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hdd.hdeco.service.AdminService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

  //field
  private final AdminService adminService;
  
  
  //관리자페이지메인홈
  @GetMapping("/adminPageHome.html")
  public String adminPageHome() {
    return "admin/adminPageHome";
  }
  
  
 }
  
	  
	