
package com.hdd.hdeco.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.UserDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.mapper.ItemOrderMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemOrderServiceImpl implements ItemOrderService {

	private final ItemOrderMapper itemOrderMapper;
	private final CartMapper cartMapper;

	@Override
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO) {
		itemOrderMapper.insertOrder(itemOrderDTO);
		return itemOrderMapper.selectUserOrder(itemOrderDTO.getUserNo());
	}

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
}
