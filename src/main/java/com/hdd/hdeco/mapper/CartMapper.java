package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;

@Mapper
public interface CartMapper {

	public int selectUserNobyId(String userId);
	public CartDTO selectItembyNo(int itemNo);
	public CartDTO selectAlreadyInCart(CartDTO alreadyCart);
	public int insertCart(CartDTO cartDTO);
	public ItemDTO selectItemByNo(int itemNo);
	public List<CartDTO> selectCartList(int userNo);
	public int deleteByItemNo(Map<String, Object> map);
	public int updateCartQuantity(Map<String, Object> map);
	public int updateCart(Map<String, Object> map);

}
