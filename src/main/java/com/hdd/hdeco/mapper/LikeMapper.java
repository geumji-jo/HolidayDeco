package com.hdd.hdeco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.LikeDTO;

@Mapper
public interface LikeMapper {

	public int selectUserNobyId(String userId);
	public int addLikeByNo(LikeDTO likeDTO);
	public int deleteLikeByNo(LikeDTO likeDTO);
	
	public int likeCheckUpdate(Map<String, Object> map);
	public List<Integer> selectItemNoInLike(int userNo);
	public int getLikeListCount(int userNo);
	
	
	public List<ItemDTO> selectLikeList(List<Integer> likeList);


}
