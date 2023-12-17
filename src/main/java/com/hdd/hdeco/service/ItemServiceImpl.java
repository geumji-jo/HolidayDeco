package com.hdd.hdeco.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.mapper.ItemMapper;
import com.hdd.hdeco.mapper.LikeMapper;
import com.hdd.hdeco.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

	private final ItemMapper itemMapper;
	private final LikeMapper likeMapper;
	private final PageUtil pageUtil;

	@Override
	public void getItemList(HttpServletRequest request, Model model) {
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
		String column = opt1.orElse("");

		// 파라미터 query가 전달되지 않는 경우 query=""로 처리
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
		String query = opt2.orElse("");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);

		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		int totalRecord = itemMapper.getItemCount();
		int recordPerPage = 21;

		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);

		List<ItemDTO> itemList = itemMapper.getItemList(map);
		model.addAttribute("itemList", itemList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil.getPagination("/item/list.do?query" + column));
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");

		// 기본값 설정
		int userNo = 0;

		// userId가 null이 아닌 경우에만 DB에서 userNo를 조회
		if (userId != null) {
		    userNo = likeMapper.selectUserNobyId(userId);
		    // 수정된 userNo를 가지고 찜 목록을 조회
		    List<Integer> likeList = likeMapper.selectItemNoInLike(userNo);
		    model.addAttribute("likeList", likeList);
		}

		model.addAttribute("itemList", itemMapper.getItemList(map));
	}

	@Override
	public void getItemByNo(Model model, HttpServletRequest request) {
	    // 기본값 설정
	    int userNo = 0;

	    // userId가 null이 아닌 경우에만 DB에서 userNo를 조회
	    HttpSession session = request.getSession();
	    String userId = (String) session.getAttribute("loginId");
	    
	    // 아이템넘버(itemNo) 받아오기
	    int itemNo = Integer.parseInt(request.getParameter("itemNo"));
	    
	    if (userId != null) {
	        userNo = likeMapper.selectUserNobyId(userId);
	        List<Integer> likeList = likeMapper.selectItemNoInLike(userNo);
	        model.addAttribute("likeList", likeList);
	    }
	    
	    // 나머지 코드에서 itemNo 활용
	    model.addAttribute("item", itemMapper.getItemByNo(itemNo));
	}


	@Override
	public ResponseEntity<byte[]> display(int itemNo) {
		ItemDTO itemDTO = itemMapper.getItemByNo(itemNo);

		ResponseEntity<byte[]> image = null;

		try {
			File mainImg = new File(itemDTO.getItemMainImg());
			if (mainImg.exists()) {
				FileInputStream inputStream = new FileInputStream(mainImg);
				byte[] imageBytes = IOUtils.toByteArray(inputStream);
				image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public ResponseEntity<byte[]> displayDetail(int itemNo) {
		ItemDTO itemDTO = itemMapper.getItemByNo(itemNo);

		ResponseEntity<byte[]> image = null;

		try {
			File detailImg = new File(itemDTO.getItemDetailImg());
			if (detailImg.exists()) {
				FileInputStream inputStream = new FileInputStream(detailImg);
				byte[] imageBytes = IOUtils.toByteArray(inputStream);
				image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	@Override
	public List<ItemDTO> searchItem(String query) {
		return itemMapper.searchItem(query);
	}
}
