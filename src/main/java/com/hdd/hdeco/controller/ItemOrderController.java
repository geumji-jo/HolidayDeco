package com.hdd.hdeco.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/payFinish.html")
	public String payFinish() {
		return "order/payFinish";
	}

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
	@PostMapping(value = "/insertOrder.do", produces = "application/json")
	public Map<String, Object> insertOrder(ItemOrderDTO itemOrderDTO, HttpServletRequest request, HttpServletResponse response) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("order", itemOrderService.insertOrder(itemOrderDTO));
	    map.put("cartList", cartService.getCartList(request, response));

	    return map;
	}
	
	
	// 결제 완료
	@PostMapping(value = "/payFinish.do")
	public String payFinish(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request, HttpServletResponse response) {
	    // 주문번호 만들기 (랜덤번호 + 주문년월일)
	    String randomNum = Math.random() + "";
	    randomNum += LocalDate.now() + "";

	    // 소수 부분만 남기고, - 없앤 후, 2023 대신 -23으로
	    int dotIndex = randomNum.indexOf(".");
	    String noDotRandomNum = randomNum.substring(dotIndex + 1);
	    noDotRandomNum = noDotRandomNum.replace("-", "");
	    String changeNum = noDotRandomNum.replace("20", "-");

	    
	    model.addAttribute("itemOrderNo", changeNum);
	    model.addAttribute("map", itemOrderService.getUserInfo(request));

	    itemOrderService.deleteCartByUserNo(request);

	    return "order/payFinish";
	}
	
	
	/* 결제 취소 */
	@GetMapping("/payFail.do")
	public String payFail(HttpServletRequest request) {
		itemOrderService.deleteOrder(request);
		return "redirect:/order/orderDetail.do?itemNo=" + request.getParameter("itemNo");
	}
	

	
	
}
