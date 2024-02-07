package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.ItemDTO;

@Mapper
public interface ItemMapper {
	// 상품 갯수 count
	public int getItemCount();

	// 상품 목록 읽어오기
	public List<ItemDTO> getItemList(Map<String, Object> map);

	// itemNo를 이용하여 아이템 정보 읽어오기
	public ItemDTO getItemByNo(int itemNo);
	
	// 상품 검색
	List<ItemDTO> searchItem(String query);
}
