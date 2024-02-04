package com.hdd.hdeco.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderCancelDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.UserDTO;
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
	
	@Override
	public void getItemEdit(HttpServletRequest request, Model model) {
		
		int itemNo = Integer.parseInt(request.getParameter("itemNo"));
		
		model.addAttribute("itemList", adminMapper.getItemByNo(itemNo));
		System.out.println("서비스 임플에 값 : " + model);
		
	}
	
	@Override
	public int itemModify(MultipartHttpServletRequest multipartRequest) throws Exception {
	    // 상품명, 상품 가격, 재고
	    String itemTitle = multipartRequest.getParameter("itemTitle");
	    String itemPrice = multipartRequest.getParameter("itemPrice");
	    int itemStock = Integer.parseInt(multipartRequest.getParameter("itemStock"));
	    int itemNo = Integer.parseInt(multipartRequest.getParameter("itemNo"));

	    ItemDTO itemDTO = new ItemDTO();
	    itemDTO.setItemTitle(itemTitle);
	    itemDTO.setItemPrice(itemPrice);
	    itemDTO.setItemStock(itemStock);
	    itemDTO.setItemNo(itemNo);

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
	    int modifyResult = adminMapper.modifyItem(itemDTO);

	    return modifyResult;
	}

	@Override
	public List<ItemDTO> searchItem(String query) {
		return adminMapper.searchItem(query);
	}
	
	@Override
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception {
		return adminMapper.orderList(itemOrderDTO);
	}
	
	@Override
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception {
		return adminMapper.orderView(itemOrderDTO);
	}
	
	@Override
	public void deliveryStatus(String itemOrderNo, String delivery) {
		Map<String, Object> map = new HashMap<>();
		map.put("itemOrderNo", itemOrderNo);
		map.put("delivery", delivery);
		adminMapper.deliveryStatus(map);
	}
	
	
	@Override
	public void UpdateItemStock(ItemDTO itemDTO) {
		adminMapper.UpdateItemStock(itemDTO);
	}
	
	//주문 취소
	@Override
	public void orderCancel(OrderCancelDTO orderCancelDTO) {
		adminMapper.orderCancel(orderCancelDTO);
	}
	
	@Override
	public void insertOrderCancel(OrderCancelDTO orderCancelDTO) throws Exception {
		adminMapper.insertOrderCancel(orderCancelDTO);
	}
	
	
//전체 유저 리스트 가져오기
 @Override
 public void getTotalUserList(HttpServletRequest request, Model model) {
  
  // 'page' 매개변수가 제공되지 않으면 1로 설정.
  Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
  int page = Integer.parseInt(opt1.orElse("1"));

  // 세션에서 'recordPerPage' 값을 가져오기. 세션에 없을 경우 10으로 기본값을 설정.
  HttpSession session = request.getSession();
  Optional<Object> opt2 = Optional.ofNullable(session.getAttribute("recordPerPage"));
  int recordPerPage = (int)(opt2.orElse(10));

  // 'order' 매개변수가 제공되지 않으면 'ASC'로 설정.(정렬)
  Optional<String> opt3 = Optional.ofNullable(request.getParameter("order"));
  String order = opt3.orElse("ASC");

  // 'column' 매개변수가 제공되지 않으면 'FAQ_NO'로 설정.(정렬칼럼)
  Optional<String> opt4 = Optional.ofNullable(request.getParameter("column"));
  String column = opt4.orElse("USER_NO");
  
  // 파라미터 query가 전달되지 않는 경우 query=""로 처리. (검색어)

	  Optional<Object> opt6 = Optional.ofNullable(request.getParameter("query"));
	  String query = (String)opt6.orElse("");
	//큰따옴표가 포함되어 있다면 제거
	  if (query.contains("\"")) {
	      query = query.replace("\"", "");
	  }

  
//파라미터 query가 전달되지 않거나 값이 null 또는 "undefined"인 경우 session에서 query 값을 가져옴. (검색어)
	/*
	 * String query = Optional.ofNullable(request.getParameter("query")) .filter(s
	 * -> !"undefined".equals(s)) .orElseGet(() -> (String)
	 * session.getAttribute("query"));
	 */

  // 데이터베이스로 전달할 맵(Map)을 생성.
  Map<String, Object> map = new HashMap<String, Object>();
  map.put("query", query);
  int totalRecord = adminMapper.getUserSearchCount(map);
  
  // column과 query를 이용해 검색된 레코드 개수를 구한다.


  // 'recordPerPage' 값이 변경되었을 때, 현재 페이지의 데이터가 없는 경우를 확인한다.
  int totalPage = (int) Math.ceil((double) totalRecord / recordPerPage);
  if ((page - 1) * recordPerPage >= totalRecord) {
      page = Math.max(totalPage, 1);
  }
  

  // 페이지 유틸리티(PageUtil)를 계산한다. (페이지네이션에 필요한 모든 정보 포함)
 
  pageUtil.setPageUtil(page, totalRecord, recordPerPage);
  map.put("begin", pageUtil.getBegin());
  map.put("order", order);
  map.put("recordPerPage", recordPerPage);
  map.put("column", column);
  
  // 지정된 범위(begin ~ end)의 목록을 가져온다.
  List<UserDTO> totalUserList = adminMapper.getTotalUserList(map);
  switch(order) {
  case "ASC" : model.addAttribute("order", "DESC"); break;  // 현재 ASC 정렬이므로 다음 정렬은 DESC이라고 Jsp에 알려준다.
  case "DESC": model.addAttribute("order", "ASC"); break;
  }
  

  
  // pagination.jsp로 전달할 정보를 저장.
  model.addAttribute("totalUserList",  totalUserList);
  model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/admin/totalUserList.html?column=" + column + "&order=" + order + "&query=" + query));
  model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
  model.addAttribute("totalRecord", totalRecord);
  model.addAttribute("page", page);
  model.addAttribute("query", query);
  
}
 
 
//휴면 유저 리스트 가져오기
 @Override
 public void getSleepUserList(HttpServletRequest request, Model model) {
	 
	  // 'page' 매개변수가 제공되지 않으면 1로 설정.
	  Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
	  int page = Integer.parseInt(opt1.orElse("1"));

	  // 세션에서 'recordPerPage' 값을 가져오기. 세션에 없을 경우 10으로 기본값을 설정.
	  HttpSession session = request.getSession();
	  Optional<Object> opt2 = Optional.ofNullable(session.getAttribute("recordPerPage"));
	  int recordPerPage = (int)(opt2.orElse(10));

	  // 'order' 매개변수가 제공되지 않으면 'ASC'로 설정.(정렬)
	  Optional<String> opt3 = Optional.ofNullable(request.getParameter("order"));
	  String order = opt3.orElse("ASC");

	  // 'column' 매개변수가 제공되지 않으면 'FAQ_NO'로 설정.(정렬칼럼)
	  Optional<String> opt4 = Optional.ofNullable(request.getParameter("column"));
	  String column = opt4.orElse("USER_NO");
	  
	  // 파라미터 query가 전달되지 않는 경우 query=""로 처리. (검색어)

		  Optional<Object> opt6 = Optional.ofNullable(request.getParameter("query"));
		  String query = (String)opt6.orElse("");
		//큰따옴표가 포함되어 있다면 제거
		  if (query.contains("\"")) {
		      query = query.replace("\"", "");
		  }

	  
	//파라미터 query가 전달되지 않거나 값이 null 또는 "undefined"인 경우 session에서 query 값을 가져옴. (검색어)
		/*
		 * String query = Optional.ofNullable(request.getParameter("query")) .filter(s
		 * -> !"undefined".equals(s)) .orElseGet(() -> (String)
		 * session.getAttribute("query"));
		 */

	  // 데이터베이스로 전달할 맵(Map)을 생성.
	  Map<String, Object> map = new HashMap<String, Object>();
	  map.put("query", query);
	  int totalRecord = adminMapper.getSleepUserSearchCount(map);
	  
	  // column과 query를 이용해 검색된 레코드 개수를 구한다.


	  // 'recordPerPage' 값이 변경되었을 때, 현재 페이지의 데이터가 없는 경우를 확인한다.
	  int totalPage = (int) Math.ceil((double) totalRecord / recordPerPage);
	  if ((page - 1) * recordPerPage >= totalRecord) {
	      page = Math.max(totalPage, 1);
	  }
	  

	  // 페이지 유틸리티(PageUtil)를 계산한다. (페이지네이션에 필요한 모든 정보 포함)
	 
	  pageUtil.setPageUtil(page, totalRecord, recordPerPage);
	  map.put("begin", pageUtil.getBegin());
	  map.put("order", order);
	  map.put("recordPerPage", recordPerPage);
	  map.put("column", column);
	  
	  // 지정된 범위(begin ~ end)의 목록을 가져온다.
	  List<UserDTO> totalUserList = adminMapper.getSleepUserList(map);
	  switch(order) {
	  case "ASC" : model.addAttribute("order", "DESC"); break;  // 현재 ASC 정렬이므로 다음 정렬은 DESC이라고 Jsp에 알려준다.
	  case "DESC": model.addAttribute("order", "ASC"); break;
	  }
	  

	  
	  // pagination.jsp로 전달할 정보를 저장.
	  model.addAttribute("sleepUserList",  totalUserList);
	  model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/admin/sleepUserList.html?column=" + column + "&order=" + order + "&query=" + query));
	  model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
	  model.addAttribute("totalRecord", totalRecord);
	  model.addAttribute("page", page);
	  model.addAttribute("query", query);
	  
	
 }
//탈퇴 유저 리스트 가져오기
 @Override
 public void getOutUserList(HttpServletRequest request, Model model) {
	  // 'page' 매개변수가 제공되지 않으면 1로 설정.
	  Optional<String> opt1 = Optional.ofNullable(request.getParameter("page"));
	  int page = Integer.parseInt(opt1.orElse("1"));

	  // 세션에서 'recordPerPage' 값을 가져오기. 세션에 없을 경우 10으로 기본값을 설정.
	  HttpSession session = request.getSession();
	  Optional<Object> opt2 = Optional.ofNullable(session.getAttribute("recordPerPage"));
	  int recordPerPage = (int)(opt2.orElse(10));

	  // 'order' 매개변수가 제공되지 않으면 'ASC'로 설정.(정렬)
	  Optional<String> opt3 = Optional.ofNullable(request.getParameter("order"));
	  String order = opt3.orElse("ASC");

	  // 'column' 매개변수가 제공되지 않으면 'FAQ_NO'로 설정.(정렬칼럼)
	  Optional<String> opt4 = Optional.ofNullable(request.getParameter("column"));
	  String column = opt4.orElse("USER_NO");
	  
	  // 파라미터 query가 전달되지 않는 경우 query=""로 처리. (검색어)

		  Optional<Object> opt6 = Optional.ofNullable(request.getParameter("query"));
		  String query = (String)opt6.orElse("");
		//큰따옴표가 포함되어 있다면 제거
		  if (query.contains("\"")) {
		      query = query.replace("\"", "");
		  }

	  
	//파라미터 query가 전달되지 않거나 값이 null 또는 "undefined"인 경우 session에서 query 값을 가져옴. (검색어)
		/*
		 * String query = Optional.ofNullable(request.getParameter("query")) .filter(s
		 * -> !"undefined".equals(s)) .orElseGet(() -> (String)
		 * session.getAttribute("query"));
		 */

	  // 데이터베이스로 전달할 맵(Map)을 생성.
	  Map<String, Object> map = new HashMap<String, Object>();
	  map.put("query", query);
	  int totalRecord = adminMapper.getOutUserSearchCount(map);
	  
	  // column과 query를 이용해 검색된 레코드 개수를 구한다.


	  // 'recordPerPage' 값이 변경되었을 때, 현재 페이지의 데이터가 없는 경우를 확인한다.
	  int totalPage = (int) Math.ceil((double) totalRecord / recordPerPage);
	  if ((page - 1) * recordPerPage >= totalRecord) {
	      page = Math.max(totalPage, 1);
	  }
	  

	  // 페이지 유틸리티(PageUtil)를 계산한다. (페이지네이션에 필요한 모든 정보 포함)
	 
	  pageUtil.setPageUtil(page, totalRecord, recordPerPage);
	  map.put("begin", pageUtil.getBegin());
	  map.put("order", order);
	  map.put("recordPerPage", recordPerPage);
	  map.put("column", column);
	  
	  // 지정된 범위(begin ~ end)의 목록을 가져온다.
	  List<UserDTO> totalUserList = adminMapper.getOutUserList(map);
	  switch(order) {
	  case "ASC" : model.addAttribute("order", "DESC"); break;  // 현재 ASC 정렬이므로 다음 정렬은 DESC이라고 Jsp에 알려준다.
	  case "DESC": model.addAttribute("order", "ASC"); break;
	  }
	  

	  
	  // pagination.jsp로 전달할 정보를 저장.
	  model.addAttribute("outUserList",  totalUserList);
	  model.addAttribute("pagination", pageUtil.getPagination(request.getContextPath() + "/admin/outUserList.html?column=" + column + "&order=" + order + "&query=" + query));
	  model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
	  model.addAttribute("totalRecord", totalRecord);
	  model.addAttribute("page", page);
	  model.addAttribute("query", query);
	  
	
	
 }
 
}
