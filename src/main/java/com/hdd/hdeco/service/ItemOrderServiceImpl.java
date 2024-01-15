
package com.hdd.hdeco.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.UserDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.mapper.ItemOrderMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemOrderServiceImpl implements ItemOrderService {

	private final ItemOrderMapper itemOrderMapper;
	private final CartMapper cartMapper;

	// 주문하기 : 주문 후 주문 정보 return
	@Override
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO) {
		itemOrderMapper.insertOrder(itemOrderDTO);
		return itemOrderMapper.selectUserOrder(itemOrderDTO.getUserNo());
	}
	
	
	@Override
	public void insertOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
		itemOrderMapper.insertOrderDetail(orderDetailDTO);
	}


	
	// user정보 조회 : 아이디를 통해 userNo 확인 
	@Override
	public UserDTO getUserInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("loginId");
		
		int userNo = cartMapper.selectUserNobyId(userId);
		
		return itemOrderMapper.getUserByUserNo(userNo);
	}

	
	
	@Override
	public List<CartDTO> getSelectItemList(HttpServletRequest request) {
		String[] items = request.getParameter("selectedItems").split(",");
		List<CartDTO> list = new ArrayList<>();
		for(String itemNo : items) {
			list.add(itemOrderMapper.getItemByNo(Integer.parseInt(itemNo)));
		}
		return list;
	}

	@Override
	public ItemDTO getItem(HttpServletRequest request) {
		int itemNo = Integer.parseInt(request.getParameter("itemNo"));
		return itemOrderMapper.getFromItem(itemNo);
	}
	
	// 결제 성공 후 카트 삭제 
	@Override
	public void deleteCartByUserNo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderMapper.deleteCartByUserNo(userNo);
	}
	
	@Override
	public void orderInfo(ItemOrderDTO itemOrderDTO) throws Exception {
		itemOrderMapper.orderInfo(itemOrderDTO);
	}
	
	@Override
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception {
		return itemOrderMapper.orderList(itemOrderDTO);
	}
	
	@Override
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception {
		return itemOrderMapper.orderView(itemOrderDTO);
	}
	
	
	// 결제실패
	@Override
	public void deleteOrder(HttpServletRequest request) {
		int itemOrderNo = Integer.parseInt(request.getParameter("itemOrderNo"));
		itemOrderMapper.deleteOrder(itemOrderNo);
	}
}
