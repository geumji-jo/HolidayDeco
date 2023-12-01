package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.CartDetailDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;

@Mapper
public interface ItemMapper {
	// 상품 갯수 count
	public int getItemCount();

	// 상품 목록 읽어오기
	public List<ItemDTO> getItemList(Map<String, Object> map);

	// itemNo를 이용하여 아이템 정보 읽어오기
	public ItemDTO getItemByNo(int itemNo);

	// userNo를 이용하여 카트 정보 읽어오기
	public CartDTO getCartByUserNo(int userNo);

	public int madeCart(int userNo); // cart 만들기

	public int addCartDetail(Map<String, Object> map); // 장바구니에 추가

	public int modifyCartDetail(Map<String, Object> map); // 장바구니 수량 변경

	public List<CartDetailDTO> getCartDetailNoByUserNo(int userNo);

	// cartdetail번호 가져오기
	public List<CartDetailDTO> getCartDetailList(int cartNo);

	// cartDetail리스트 가져오기
	public CartDetailDTO getCartDetailByCartNo(int cartNo);

	public CartDetailDTO confirmItemInCart(Map<String, Object> map);

	public int addOrder(ItemOrderDTO orderDTO);

	public int addOrderList(Map<String, Object> map); // OrderList만들기 public List<OrderDTO> getOrderList(int userNo);
	// OrderList가져오기 public UsersDTO getOrderNoByUserNo(int cartNo);
	// cartNo를 통해서 orderNo가져오기 public ItemOrderDTO confirmUsersInOrder(Map<String,
	// Object> map);

	public List<CartDTO> deleteCart(int cartNo); // cartNo삭제하기

}
