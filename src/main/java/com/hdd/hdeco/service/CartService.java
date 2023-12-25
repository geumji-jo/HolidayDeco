package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdd.hdeco.domain.CartDTO;

public interface CartService {
	
	public String addCart(HttpServletRequest request);
	public List<CartDTO> getCartList(HttpServletRequest request, HttpServletResponse response);
  public int getUserNo(HttpServletRequest request);
	public int deleteCart(List<String> itemNoArr, HttpServletRequest request);
	public void updateCartQuantity(int quantity, int itemNo);
	public int cartPrice(int userNo);
	 
}
