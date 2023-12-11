package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.UserDTO;

public interface ItemOrderService {
	
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO);
	public UserDTO getUserInfo(HttpServletRequest request);
	public List<CartDTO> getSelectItemList(HttpServletRequest request);
	public ItemDTO getItem(HttpServletRequest request);
	 
}
