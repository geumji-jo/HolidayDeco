package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.ItemDTO;

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
}
