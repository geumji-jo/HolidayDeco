package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.UserDTO;

public interface ItemOrderService {
	/*
	 * public String getToken(); // 아임포트 토큰가져오기
	 */	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO);
	public UserDTO getUserInfo(HttpServletRequest request);
	public List<CartDTO> getSelectItemList(HttpServletRequest request);
	public ItemDTO getItem(HttpServletRequest request);
	public void deleteCartByUserNo(HttpServletRequest request);
	public void deleteOrder(HttpServletRequest request);
	public void insertOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
	public void orderInfo(ItemOrderDTO itemOrderDTO) throws Exception;
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception;
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception;
}

