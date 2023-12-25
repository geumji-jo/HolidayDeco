package com.hdd.hdeco.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.service.CartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/cart")
@Controller
public class CartController {
	private final CartService cartService;

	@GetMapping("/getCartList.do")
	public String getCartList(HttpServletRequest request, HttpServletResponse response, Model model) {
		int userNo = cartService.getUserNo(request);

    // 요청 파라미터
		List<CartDTO> cartList = cartService.getCartList(request, response);
		model.addAttribute("cartList", cartList);
		model.addAttribute("userNo", cartService.getUserNo(request));
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 장바구니 정보 
		// 장바구니 전체 금액 
		int cartPrice = cartService.cartPrice(userNo);
		
		// 장바구니 전체 금액에 따른 배송비 
		// 배송비 (4만원 이상 무료, 4만원 미만 3,000원) 
		int deliveryFee = cartPrice > 40000 ? 0 : 3000;
		
		map.put("cartList", cartList);
		map.put("quantity", cartList.size());
		map.put("cartPrice", cartPrice);
		map.put("deliveryFee", deliveryFee);
		map.put("orderTotal", cartPrice + deliveryFee);
		
		
		return "order/cart";
	}

	@ResponseBody
	@PostMapping(value = "/addCart.do", produces = "application/json")
	public Map<String, Object> addCart(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		map.put("msg", cartService.addCart(request));
		return map;
	}

	@GetMapping("/deleteCart.do")
	public String deleteCart(@RequestParam("itemNoArr") List<String> itemNoArr, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		int deleteResult = cartService.deleteCart(itemNoArr, request);
		redirectAttributes.addFlashAttribute("deleteResult", deleteResult);
		return "redirect:/cart/getCartList.do";
	}

	@ResponseBody
	@GetMapping("/updateCartQuantity.do")
	public void updateCartQuantity(@RequestParam("quantity") int quantity, @RequestParam("itemNo") int itemNo) {
		cartService.updateCartQuantity(quantity, itemNo);
	}

}
