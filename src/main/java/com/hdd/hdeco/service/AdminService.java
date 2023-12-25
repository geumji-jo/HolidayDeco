package com.hdd.hdeco.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface AdminService {

	// 상품 관리 
	
	public void getItemByNo(Model model,HttpServletRequest request);
	public void getItemManageList(HttpServletRequest request, Model model);
	public int uploadItem(MultipartHttpServletRequest multipartRequest) throws Exception;
  public ResponseEntity<byte[]> display(int itemNo);
  public ResponseEntity<byte[]> displayDetail(int itemNo);
  public int deleteItem(int itemNo);
  
}