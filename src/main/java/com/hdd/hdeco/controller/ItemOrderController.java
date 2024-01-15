package com.hdd.hdeco.controller;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.service.CartService;
import com.hdd.hdeco.service.ItemOrderService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class ItemOrderController {
	
	private final ItemOrderService itemOrderService;
	private final CartService cartService;
	private final CartMapper cartMapper;
	

	@PostMapping("/orderAll.do")
	public String orderAll(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", cartService.getCartList(request, response));
		return "order/orderDetails";
	}
	
	@PostMapping("/orderSelect.do")
	public String orderSelect(HttpServletRequest request, Model model,ItemOrderDTO itemOrderDTO) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", itemOrderService.getSelectItemList(request));


		return "order/orderDetails";
	}
	

	@ResponseBody
	@PostMapping(value = "/insertOrder.do", produces = "application/json")
	public Map<String, Object> insertOrder(ItemOrderDTO itemOrderDTO, OrderDetailDTO orderDetailDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// 캘린더 호출
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);  // 연도 추출
		String ym = year + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);  // 월 추출
		String ymd = ym +  new DecimalFormat("00").format(cal.get(Calendar.DATE));  // 일 추출
		String subNum = "";  // 랜덤 숫자를 저장할 문자열 변수
		
		for(int i = 1; i <= 6; i ++) {  // 6회 반복
			subNum += (int)(Math.random() * 10);  // 0~9까지의 숫자를 생성하여 subNum에 저장
		}
		
		String itemOrderNo = ymd + "_" + subNum;  // [연월일]_[랜덤숫자] 로 구성된 문자
		itemOrderDTO.setItemOrderNo(itemOrderNo);
		orderDetailDTO.setItemOrderNo(itemOrderNo);
		Map<String, Object> map = new HashMap<>();
		map.put("order", itemOrderService.insertOrder(itemOrderDTO));
		itemOrderService.insertOrderDetail(orderDetailDTO);
	
    return map;
}
	
	
	// 결제 완료
	@GetMapping(value = "/payFinish.do")
	public String payFinish(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request)  throws Exception {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		List<ItemOrderDTO> orderList = itemOrderService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
		model.addAttribute("map", itemOrderService.getUserInfo(request));
	    return "order/payFinish";
	}
	
  //주문 목록
	@GetMapping(value = "/orderList.do")
	public void getOrderList(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		List<ItemOrderDTO> orderList = itemOrderService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
		model.addAttribute("map", itemOrderService.getUserInfo(request));
	}
	
	// 주문 상세 목록 
	@GetMapping(value="/orderView.do")
	public void getOrderList(HttpServletRequest request,  @RequestParam("n") String itemOrderNo, ItemOrderDTO itemOrderDTO, Model model)throws Exception {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		itemOrderDTO.setItemOrderNo(itemOrderNo);

		List<OrderListDTO> orderView = itemOrderService.orderView(itemOrderDTO);

		model.addAttribute("orderView", orderView);
	}
	
	// 결제 취소 
	@GetMapping("/payFail.do")
	public String payFail(HttpServletRequest request) {
		itemOrderService.deleteOrder(request);
		return "redirect:/order/orderDetail.do?itemNo=" + request.getParameter("itemNo");
	}
	

	
	
}
