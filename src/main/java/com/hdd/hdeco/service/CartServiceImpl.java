
package com.hdd.hdeco.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.mapper.CartMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

	private final CartMapper cartMapper;
	
	@Override
	public String addCart(HttpServletRequest request) {
	    // userId 가져오기
	    HttpSession session = request.getSession();
	    String userId = (String) session.getAttribute("loginId");

	    // 요청 파라미터
	    int userNo = cartMapper.selectUserNobyId(userId);
	    int itemNo = Integer.parseInt(request.getParameter("itemNo"));
	    int quantity = Integer.parseInt(request.getParameter("quantity"));

	    // 상품 번호로 상품 정보(이름, 이미지, 가격) 카트에 담기
	    CartDTO cartDTO = cartMapper.selectItembyNo(itemNo);

	    // 장바구니에 상품 담기
	    cartDTO.setUserNo(userNo);
	    cartDTO.setItemNo(itemNo); 
	    cartDTO.setQuantity(quantity);

	    // 이미 장바구니에 담긴 상품이 있는지 확인
	    CartDTO existingCartItem = cartMapper.selectAlreadyInCart(cartDTO);

	    if (existingCartItem == null) {
	        // 장바구니에 상품이 없으면 insert
	        int insertResult = cartMapper.insertCart(cartDTO);
	        if (insertResult == 1) {
	            return "장바구니에 상품이 담겼습니다. 장바구니로 이동하시겠습니까?";
	        } else {
	            return "장바구니 상품 추가가 실패했습니다.";
	        }
	    } else {
	        // 이미 장바구니에 담긴 상품이면 수량 업데이트
	        Map<String, Object> updateMap = new HashMap<>();
	        updateMap.put("quantity", quantity);
	        updateMap.put("itemNo", itemNo);
	        int updateResult = cartMapper.updateCart(updateMap);
	        if (updateResult == 1) {
	            return "[장바구니 업데이트 완료] 장바구니로 이동하시겠습니까?";
	        } else {
	            return "장바구니 상품 업데이트에 실패했습니다.";
	        }
	    }
	}


	@Override
	public List<CartDTO> getCartList(HttpServletRequest request, HttpServletResponse response) {

		// userId 가져오기
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId"); 

		// userNo 가져오기
		int userNo = cartMapper.selectUserNobyId(userId);
		List<CartDTO> cartList = cartMapper.selectCartList(userNo);
		
		return cartList;
	}

	@Override
	public int getUserNo(HttpServletRequest request) {
		// userId 가져오기
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");

		// userNo 가져오기
		int userNo = cartMapper.selectUserNobyId(userId);

		return userNo;

	}

	@Override
	public int deleteCart(List<String> itemNoArr, HttpServletRequest request) {
		// userId 가져오기
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");

		// userNo 가져오기 
		int userNo = cartMapper.selectUserNobyId(userId);

		Map<String, Object> map = new HashMap<>();
		map.put("userNo", userNo);

		int deleteResult = 0;
		int length = itemNoArr.size();

		for (int i = 0; i < length; i++) {
			map.put("itemNo", itemNoArr.get(i));
			deleteResult += cartMapper.deleteByItemNo(map);
		}
		return deleteResult;
	}

	@Override
	public void updateCartQuantity(int quantity, int itemNo) {

		Map<String, Object> map = new HashMap<>();
		map.put("quantity", quantity);
		map.put("itemNo", itemNo);

		cartMapper.updateCartQuantity(map);

	}

}
