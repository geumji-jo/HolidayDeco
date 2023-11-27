package com.hdd.hdeco.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.CartDetailDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.mapper.ItemMapper;
import com.hdd.hdeco.util.PageUtil;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService{
	
	private final ItemMapper itemMapper;
  private final PageUtil pageUtil;
  
  
  @Override
  public void getItemList(HttpServletRequest request, Model model) {
  	Optional<String> opt1 = Optional.ofNullable(request.getParameter("column"));
  	String column = opt1.orElse("");
  	
  	//파라미터 query가 전달되지 않는 경우 query=""로 처리
  	Optional<String> opt2 = Optional.ofNullable(request.getParameter("query"));
  	String query = opt2.orElse("");
  	
  	Map<String, Object> map = new HashMap<String, Object>();
  	map.put("column", column);
  	map.put("query", query);
  	
  	Optional<String> opt3 = Optional.ofNullable(request.getParameter("page"));
  	int page = Integer.parseInt(opt3.orElse("1"));
  	int totalRecord = itemMapper.getItemCount();
  	int recordPerPage = 20;
  	
  	pageUtil.setPageUtil(page, totalRecord, recordPerPage);
  	map.put("begin", pageUtil.getBegin());
  	map.put("recordPerPage", recordPerPage);
  	
  	List<ItemDTO> itemList = itemMapper.getItemList(map);
  	model.addAttribute("itemList", itemList);
  	model.addAttribute("beginNo", totalRecord - (page - 1) * recordPerPage);
  	model.addAttribute("pagination", pageUtil.getPagination("/item/list.do?query" + column));
  	
  	
  }
  
  @Override
  public void getItemByNo(int itemNo, Model model) {
  	 model.addAttribute("item", itemMapper.getItemByNo(itemNo));
  }
  
  @Override
  public ResponseEntity<byte[]> display(int itemNo) {
  	ItemDTO itemDTO = itemMapper.getItemByNo(itemNo);
  	
  	ResponseEntity<byte[]> image = null;
  	
  	try {
  		File mainImg = new File(itemDTO.getItemMainImg());
  		if(mainImg.exists()) {
  			FileInputStream inputStream = new FileInputStream(mainImg);
  			byte[] imageBytes = IOUtils.toByteArray(inputStream);
  			image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
  		}
  	}catch (Exception e) {
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
  		if(detailImg.exists()) {
  			FileInputStream inputStream = new FileInputStream(detailImg);
  			byte[] imageBytes = IOUtils.toByteArray(inputStream);
  			image = new ResponseEntity<>(imageBytes, HttpStatus.OK);
  		}
  	}catch (Exception e) {
  		e.printStackTrace();
		}
  	return image;
  }
  
  @Override
  public Map<String, Object> addCartDetail(HttpServletRequest request) {
     try {
        
        
        int userNo = Integer.parseInt(request.getParameter("userNo"));
        int cdNo = Integer.parseInt(request.getParameter("itemNo"));
        int count = Integer.parseInt(request.getParameter("count"));
     
        // 카트 존재 확인
        CartDTO cartDTO = itemMapper.getCartByUserNo(userNo);

        // 카트 없으면 만들기
        if(cartDTO == null) {
           int insertResult = itemMapper.madeCart(userNo);  // int 결과는 어차피 1 아니면 0
           if(insertResult == 1) {
              cartDTO = itemMapper.getCartByUserNo(userNo);
           }
        }
        
        // 파라미터 전달용 Map
        Map<String, Object> map = new HashMap<>();
        map.put("cartNo", cartDTO.getCartNo());
        map.put("itemNo", cdNo);
        map.put("count", count);

        // 이 장바구니에 이 CD를 장바구니에 담은 적이 있는지 점검
        CartDetailDTO cartDetailDTO = itemMapper.confirmItemInCart(map);
        
        // 장바구니 추가 및 수량 업데이트 결과
        int addCartDetailResult = 0;
        
        // 장바구니에 담은 적이 없는 CD이면 장바구니에 추가
        if(cartDetailDTO == null) {
           addCartDetailResult = itemMapper.addCartDetail(map);            
        }         
        // 이미 장바구니에 담은 CD이면 수량 업데이트
        else {
           addCartDetailResult = itemMapper.modifyCartDetail(map);
        }
        
        // 장바구니를 담고 이동하는데 필요한 것들 
        Map<String, Object> resultMap = new HashMap<String,Object>();
        resultMap.put("addCartDetailResult", addCartDetailResult);
        resultMap.put("cartNo", cartDTO.getCartNo());
        return resultMap;
     }catch (NumberFormatException e) {
        e.printStackTrace();
     }catch(Exception e) {
        e.printStackTrace();
     }
     return null;
     
  }
  
  

/*
  


  @Override
  public void getCartDetailList(int cartNo, Model model) {
     // getCartByCartNo -> 1번 보내서 1번 카트 가져오기 
     // public List<Map<String, Object>> getCartDetailList(int cartNo) {
     // getCartDetailByCartNo -> list로 끌고와야함 -> 안은 map으로 <"cdNo" : cdNo> <"count" count">
     // Map<String, Object> map = hashmap...
     // map.put("cdNo", cdNo)
     // map.put(
     //list.put(map) 
     // return list
     // 리스트는 model 쓰기가 애매하다 1개의 DTO면 모델을 쓸만하지만 여러개의 DTO 경우는 애매해다
     List<CartDetailDTO> cartDetailList = shopMapper.getCartDetailList(cartNo);
     model.addAttribute("cartDetailList", cartDetailList);
     model.addAttribute("cartNo", cartNo);
  }
  
  
     cart테이블
     cartNo madeAt userNo
     
     cartDetail테이블
     cartDetailNo userNo cartNo cdNo
     AI           userNo cartNo cdNo
  
  */
}
