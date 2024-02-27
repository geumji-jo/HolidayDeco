package com.hdd.hdeco.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.UserDTO;

public interface ItemOrderService {
	
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO);
	public UserDTO getUserInfo(HttpServletRequest request);
	public List<CartDTO> getSelectItemList(HttpServletRequest request);
	public ItemDTO getItem(HttpServletRequest request);
	public void deleteCartByUserNo(HttpServletRequest request);
	public void deleteOrder(HttpServletRequest request);
	public void insertOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
	public void orderInfo(ItemOrderDTO itemOrderDTO) throws Exception;
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception;
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception;
	
	public String getToken() throws Exception;
	public int paymentInfo(String imp_uid, String access_token) throws IOException;
	public void payMentCancel(String access_token, String imp_uid, int amount, String reason) throws IOException;
	
	//바로구매 기능에 관련된 서비스
	public CartDTO addGoBuyItem(HttpServletRequest request);
	public List<CartDTO> getGoBuyItemList(HttpServletRequest request, HttpServletResponse response);
	public int getGoBuyItemPrice(int userNo);
	public void insertGoBuyOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception;
}
