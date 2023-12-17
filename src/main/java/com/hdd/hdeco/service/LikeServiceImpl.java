
package com.hdd.hdeco.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.LikeDTO;
import com.hdd.hdeco.mapper.LikeMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikeServiceImpl implements LikeService {

	private final LikeMapper likeMapper;
	
		// 클래스리스트 찜목록 삽입/삭제
		@Override
		public Map<String, Object> getLikeByNo(HttpServletRequest request) {
			
			// userId 가져오기 
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("loginId");
			
			// 요청 파라미터
			int userNo = likeMapper.selectUserNobyId(userId);
			int itemNo = Integer.parseInt(request.getParameter("itemNo"));
			
			LikeDTO likeDTO = new LikeDTO();
			likeDTO.setItemNo(itemNo);
			likeDTO.setUserNo(userNo);
			
			List<Integer> likeList = likeMapper.selectItemNoInLike(userNo);
			int addResult = 0;
			int deleteResult = 0;
			
			if (likeList.isEmpty() || !likeList.contains(itemNo)) {
		        addResult = likeMapper.addLikeByNo(likeDTO);
		    } else {
		        deleteResult = likeMapper.deleteLikeByNo(likeDTO);
		    }
			
		  // 찜 목록 변경 후 LIKE_T 테이블 업데이트
	    Map<String, Object> updateParams = new HashMap<>();
	    updateParams.put("userNo", userNo);
	    updateParams.put("itemNo", itemNo);

	    // 여기에서 likeCheckUpdate 쿼리를 호출하여 LIKE_T 테이블 업데이트
	    likeMapper.likeCheckUpdate(updateParams);

	    Map<String, Object> resultMap = new HashMap<>();
	    resultMap.put("addResult", addResult);
	    resultMap.put("deleteResult", deleteResult);
			return resultMap;

			
		}
		
		// 찜목록 안에 찜 삭제
		@Override
		public int getLikeDeleteByNo(HttpServletRequest request) {
			
			String stritemNo = request.getParameter("itemNo");
			int itemNo = 0;
			if(stritemNo != null && stritemNo.isEmpty() == false) {
				itemNo = Integer.parseInt(stritemNo);
			}
			
			HttpSession session = request.getSession();
			int userNo = Integer.parseInt(session.getAttribute("loginId").toString());
			
			LikeDTO likeDTO = new LikeDTO();
			likeDTO.setItemNo(itemNo);
			likeDTO.setUserNo(userNo);
			
			int deleteResult = likeMapper.deleteLikeByNo(likeDTO);
			
			return deleteResult;
		}
		
		
		// 찜목록 리스트
		@Override
		public void getLikeList(HttpServletRequest request, Model model) {
			
			// userId 가져오기 
			HttpSession session = request.getSession();
			String userId = (String)session.getAttribute("loginId");
			
			// 요청 파라미터
			int userNo = likeMapper.selectUserNobyId(userId);
		  // likeCheck 가져오기
	    Map<String, Object> map = new HashMap<>();
	    map.put("userNo", userNo);
	    map.put("likeCheck", likeMapper.likeCheckUpdate(map));

					
			// userNo를 mapper에 주고 itemNo를 전부 뽑아오는 코드
			List<Integer> likeList = likeMapper.selectItemNoInLike(userNo);
			List<ItemDTO> itemList = null;
			if(likeList.size() == 0) {
				model.addAttribute("itemList", itemList);			
			}else {
				itemList = likeMapper.selectLikeList(likeList);			
				model.addAttribute("itemList", itemList);
			}

		}
		
}
