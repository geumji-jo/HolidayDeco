package com.hdd.hdeco.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;


public interface ItemService {
	
  public void getItemList(HttpServletRequest request, Model model);
  public void getItemByNo(int itemNo, Model model);
  
  public ResponseEntity<byte[]> display(int itemNo);
  public ResponseEntity<byte[]> displayDetail(int itemNo);
  
	/*
	 * public Map<String, Object> addCartDetail(HttpServletRequest request); public
	 * void getCartDetailList(int cartNo, Model model);
	 */

}
