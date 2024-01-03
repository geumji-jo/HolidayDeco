package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hdd.hdeco.domain.ItemDTO;

public interface AdminService {

	// 상품 관리 
	public void getItemByNo(Model model,HttpServletRequest request);
	public void getItemManageList(HttpServletRequest request, Model model);
	public int uploadItem(MultipartHttpServletRequest multipartRequest) throws Exception;
  public ResponseEntity<byte[]> display(int itemNo);
  public ResponseEntity<byte[]> displayDetail(int itemNo);
  public int deleteItem(int itemNo);
  public void getItemEdit(HttpServletRequest request, Model model);	
  public int itemModify(MultipartHttpServletRequest multipartRequest) throws Exception;
  List<ItemDTO> searchItem(String query);
}