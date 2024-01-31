package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderCancelDTO;
import com.hdd.hdeco.domain.OrderListDTO;

@Mapper
public interface AdminMapper {

	//상품 갯수 count
	public int getItemCount();
	
	//상품 목록 
	public List<ItemDTO> getItemManageList(Map<String, Object> map);
	
	// 상품 등록 
	public int uploadItem(ItemDTO itemDTO);
	
	// 상품 삭제 
	public int deleteItem(int itemNo);

	// itemNo를 이용하여 아이템 정보 읽어오기
	public ItemDTO getItemByNo(int itemNo);
	
	// 상품 수정 
	public int modifyItem(ItemDTO itemDTO);
	
	// 검색
	List<ItemDTO> searchItem(String query);
	
	// 주문 목록
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception;
	
	// 특정 주문 목록
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception;
	
	// 배송 상태 
	public void deliveryStatus (Map<String, Object> map);
	
	// 상품 재고 업데이트
	public int UpdateItemStock(ItemDTO itemDTO);
	
	//주문취소
	public void orderCancel(OrderCancelDTO orderCancelDTO);
	
	public int insertOrderCancel(OrderCancelDTO orderCancelDTO);
}
