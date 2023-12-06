
package com.hdd.hdeco.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.UserDTO;
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
		cartDTO.setItemNo(itemNo); // 변경된 부분
		cartDTO.setQuantity(quantity); // 변경된 부분

		if (cartMapper.selectAlreadyInCart(cartDTO) == null) {
			int insertResult = cartMapper.insertCart(cartDTO);
			if (insertResult == 1) {
				return "장바구니에 상품이 담겼습니다. 장바구니로 이동하시겠습니까?";
			}
		}
		return "이미 장바구니에 담긴 상품입니다. 장바구니로 이동하시겠습니까?";
	}

	@Override
	public List<CartDTO> getCartList(HttpServletRequest request, HttpServletResponse response) {

		// userId 가져오기
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId"); // 수정

		// 로그인 여부 확인
		if (userId == null) {
			// 로그인되지 않은 상태 처리
			try {
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('로그인이 필요한 서비스입니다.');");
				out.println("location.href='/user/login.html';"); // 수정: 로그인 페이지로 이동
				out.println("</script>");
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Collections.emptyList(); // 빈 리스트 반환 또는 필요에 따라 다른 처리 수행
		}

		// userNo 가져오기
		int userNo = cartMapper.selectUserNobyId(userId);

		// userNo가 null인 경우에 대한 처리 추가
		if (userNo == 0) {
			// userNo가 0이라면 해당 userId에 대한 사용자 정보가 없는 경우입니다.
			// 처리 방법은 상황에 따라 결정하셔야 합니다.
			// 예를 들어, 로그인 페이지로 이동하도록 리다이렉트하거나, 다른 처리를 수행할 수 있습니다.
			// 아래는 리다이렉트하는 예시입니다.
			try {
				response.sendRedirect("/user/login.html"); // 로그인 페이지로 이동하도록 설정해주세요.
			} catch (IOException e) {
				e.printStackTrace();
			}
			return Collections.emptyList(); // 빈 리스트 반환 또는 필요에 따라 다른 처리 수행
		}

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
