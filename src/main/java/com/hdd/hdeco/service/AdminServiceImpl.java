package com.hdd.hdeco.service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
		
		Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
		String column = opt1.orElse("");
		
		Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
		String query = opt2.orElse("");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("column", column);
		map.put("query", query);
		
		Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
		int page = Integer.parseInt(opt3.orElse("1"));
		int totalRecord = adminMapper.getItemCount();
		int recordPerPage = 10;
		
		pageUtil.setPageUtil(page, totalRecord, recordPerPage);
		map.put("begin", pageUtil.getBegin());
		map.put("recordPerPage", recordPerPage);
		
		List<ItemDTO> itemList =adminMapper.getItemManageList(map);
		model.addAttribute("itemList", itemList);
		model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
		model.addAttribute("pagination", pageUtil.getPagination("/admin/itemManageList.do?query" + column));

		
	}

	
	


	@Transactional
	@Override
	public int uploadItem(MultipartHttpServletRequest multipartRequest) throws Exception {
	    // 상품명, 상품 가격, 재고
	    String itemTitle = multipartRequest.getParameter("itemTitle");
	    String itemPrice = multipartRequest.getParameter("itemPrice");
	    int itemStock = Integer.parseInt(multipartRequest.getParameter("itemStock"));

	    ItemDTO itemDTO = new ItemDTO();
	    itemDTO.setItemTitle(itemTitle);
	    itemDTO.setItemPrice(itemPrice);
	    itemDTO.setItemStock(itemStock);

	    MultipartFile mainImgFile = multipartRequest.getFile("itemMainImg");
	    if (mainImgFile != null && !mainImgFile.isEmpty()) {
	        // 첨부파일 HDD에 저장하는 코드
	        String path = myFileUtil.getPath();
	        String mainImgFilename = myFileUtil.getFilesystemName(mainImgFile.getOriginalFilename());
	        File dir = new File(path);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        // UUID를 사용하여 고유한 파일명 생성
	        String uniqueFilename = UUID.randomUUID().toString() + "_" + mainImgFilename;

	        // 새로운 파일 경로
	        String fullFilePath = path + "/" + uniqueFilename; 
	        Path filePath = Paths.get(fullFilePath).toAbsolutePath();
	        mainImgFile.transferTo(filePath.toFile());
	        itemDTO.setItemMainImg(fullFilePath);
	    }

	    MultipartFile detailImgFile = multipartRequest.getFile("itemDetailImg");
	    if (detailImgFile != null && !detailImgFile.isEmpty()) {
	        // 첨부파일 HDD에 저장하는 코드
	        String path = myFileUtil.getPath();
	        String detailImgFilename = myFileUtil.getFilesystemName(detailImgFile.getOriginalFilename());
	        File dir = new File(path);
	        if (!dir.exists()) {
	            dir.mkdirs();
	        }

	        // UUID를 사용하여 고유한 파일명 생성
	        String uniqueFilename = UUID.randomUUID().toString() + "_" + detailImgFilename;

	        // 새로운 파일 경로
	        String fullFilePath = path + "/" + uniqueFilename; 
	        Path filePath = Paths.get(fullFilePath).toAbsolutePath();
	        detailImgFile.transferTo(filePath.toFile());
	        itemDTO.setItemDetailImg(fullFilePath);
	    }
	    int uploadItemResult = adminMapper.uploadItem(itemDTO);

	    return uploadItemResult;
	}

	@Override
  public ResponseEntity<byte[]> display(int itemNo) {
     ItemDTO itemDTO = adminMapper.getItemByNo(itemNo);
     ResponseEntity<byte[]> image = null;
     try {
        File itemMainImg = new File(itemDTO.getItemMainImg());
        if (itemMainImg.exists()) {
              FileInputStream inputStream = new FileInputStream(itemMainImg);
              byte[] imageBytes = IOUtils.toByteArray(inputStream);
              image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
        } 
     } catch(Exception e) {
        e.printStackTrace();
     }
     return image;
  }
  
  @Override
  public ResponseEntity<byte[]> displayDetail(int itemNo) {
  	ItemDTO itemDTO = adminMapper.getItemByNo(itemNo);
	   ResponseEntity<byte[]> image = null;
	   try {
		   File itemDetailImg = new File(itemDTO.getItemDetailImg());
		   if (itemDetailImg.exists()) {
			   FileInputStream inputStream = new FileInputStream(itemDetailImg);
			   byte[] imageBytes = IOUtils.toByteArray(inputStream);
			   image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
		   } 
	   } catch(Exception e) {
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
