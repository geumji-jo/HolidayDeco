package com.hdd.hdeco.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.service.CartService;
import com.hdd.hdeco.service.ItemOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class ItemOrderController {
	
	private final ItemOrderService itemOrderService;
	private final CartService cartService;

	@PostMapping("/orderAll.do")
	public String orderAll(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", cartService.getCartList(request, response));
		return "order/orderDetails";
	}
	
	@PostMapping("/orderSelect.do")
	public String orderSelect(HttpServletRequest request, Model model) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", itemOrderService.getSelectItemList(request));
		return "order/orderDetails";
	}	
	
	
	// 결제하기 버튼 누르면 실행
	@ResponseBody
	@PostMapping(value="/insertOrder.do", produces="application/json") 
	public Map<String, Object> insertOrder(ItemOrderDTO itemOrderDTO, Model model){
		Map<String, Object> map = new HashMap<>();
		map.put("order", itemOrderService.insertOrder(itemOrderDTO));
		return map;
	}
	
}
