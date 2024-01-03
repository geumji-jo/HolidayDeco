package com.hdd.hdeco.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.UserDTO;

@Mapper
public interface ItemOrderMapper {

	public int insertOrder(ItemOrderDTO itemOrderDTO);
	public ItemOrderDTO selectUserOrder(int userNo);
	public UserDTO getUserByUserNo(int userNo);
	public CartDTO getItemByNo(int itemNo);
	public ItemDTO getFromItem(int itemNo);
	public int deleteOrder(int itemOrderNo);
	public int deleteCartByUserNo(int userNo);
}
