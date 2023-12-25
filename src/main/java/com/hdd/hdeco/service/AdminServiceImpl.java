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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.mapper.AdminMapper;
import com.hdd.hdeco.util.MyFileUtil;
import com.hdd.hdeco.util.PageUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
	
	private final AdminMapper adminMapper;
	private final PageUtil pageUtil;
	private final MyFileUtil myFileUtil;
	
	@Override
	public void getItemByNo(Model model, HttpServletRequest request) {
	    // 기본값 설정
	    int userNo = 0;
     // userId가 null이 아닌 경우에만 DB에서 userNo를 조회
	    HttpSession session = request.getSession();
	    String userId = (String) session.getAttribute("loginId");
	    
	    // 아이템넘버(itemNo) 받아오기
	    int itemNo = Integer.parseInt(request.getParameter("itemNo"));
	    
	    // 나머지 코드에서 itemNo 활용
	    model.addAttribute("item", adminMapper.getItemByNo(itemNo));
	}
	
	@Override
	public void getItemManageList(HttpServletRequest request, Model model) {
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("query"));
		String query = opt1.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("query", query);
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt2.orElse("1"));
		
		int totalRecord = adminMapper.getItemCount();
		
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		
		
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<ItemDTO> itemList =adminMapper.getItemManageList(map);
		model.addAttribute("itemList", itemList);
		model.addAttribute("pagination", pageUtil.getPagination("/item/itemManageList.do?query=" + query));

		
	}

	
	@Transactional
	@Override
	public int uploadItem(MultipartHttpServletRequest multipartRequest) throws Exception {
		String itemTitle = multipartRequest.getParameter("itemTitle");
		String itemPrice = multipartRequest.getParameter("itemPrice");
		int itemStock = Integer.parseInt(multipartRequest.getParameter("itemStock"));
		
		ItemDTO itemDTO = new ItemDTO();
		itemDTO.setItemTitle(itemTitle);
		itemDTO.setItemPrice(itemPrice);
		itemDTO.setItemStock(itemStock);
		
		MultipartFile itemMainImgFile = multipartRequest.getFile("MainImg");
		
		if(itemMainImgFile != null && itemMainImgFile.isEmpty() == false) {
			 // 첨부파일 HDD에 저장하는 코드
			String path = myFileUtil.getItemImgPath();
			String itemMainImgFilename= myFileUtil.getFilesystemName(itemMainImgFile.getOriginalFilename());
			File dir = new File(path);
			if(dir.exists() == false) {
				dir.mkdirs();
			}
			File file = new File(dir, itemMainImgFilename);
			itemMainImgFile.transferTo(file); // 실제 서버에 저장
			itemDTO.setItemMainImg(path + itemMainImgFilename);
			
		}
		 MultipartFile detailImgFile = multipartRequest.getFile("detailImg");
     if(detailImgFile != null && detailImgFile.isEmpty() == false) {
        // 첨부파일 HDD에 저장하는 코드
        String path = myFileUtil.getItemImgPath();
        String detailImgFilename = myFileUtil.getFilesystemName(detailImgFile.getOriginalFilename());
        File dir = new File(path);
        if(dir.exists() == false) {
           dir.mkdirs();
        }
        File file = new File(dir, detailImgFilename);
        detailImgFile.transferTo(file);   // 실제 서버에 저장
        itemDTO.setItemDetailImg(path + detailImgFilename);         
     }
     
     int uploadItemResult = adminMapper.uploadItem(itemDTO);
	
     
	  return uploadItemResult;
	}


	@Override
	public ResponseEntity<byte[]> display(int itemNo) {
		ItemDTO itemDTO = adminMapper.getItemByNo(itemNo);

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
		ItemDTO itemDTO = adminMapper.getItemByNo(itemNo);

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
	public int deleteItem(int itemNo) {
		int deleteResult = adminMapper.deleteItem(itemNo);
		return deleteResult;
	}

}
