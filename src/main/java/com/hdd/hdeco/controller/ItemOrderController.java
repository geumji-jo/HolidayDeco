package com.hdd.hdeco.controller;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.PaymentCancelDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.service.CartService;
import com.hdd.hdeco.service.ItemOrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/order")
@Controller
public class ItemOrderController {
	
	private final ItemOrderService itemOrderService;
	private final CartService cartService;
	private final CartMapper cartMapper;
	
	// 아임포트 토큰 (성공)
	private IamportClient client = new IamportClient("3232467880861681","lSBFzXMaebapZaF0xpcutq4Y2UzDelbeDrNqKS8Xkz8RGKDlnz4eBBJY3PzY2rcjW3VeINQdzO5LpFwH");
  
	// 결제 검증 (성공) 
	@ResponseBody
	@RequestMapping(value = "/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid(Model model, Locale locale, HttpSession session, @PathVariable(value = "imp_uid") String imp_uid) throws IamportResponseException, IOException {
	  System.out.println(client.paymentByImpUid(imp_uid));
		return client.paymentByImpUid(imp_uid);
	}

	// 장바구니 전체 주문 
	@PostMapping("/orderAll.do")
	public String orderAll(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", cartService.getCartList(request, response));
		return "order/orderDetails";
	}
	
	// 장바구니 선택 주문 
	@PostMapping("/orderSelect.do")
	public String orderSelect(HttpServletRequest request, Model model,ItemOrderDTO itemOrderDTO) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", itemOrderService.getSelectItemList(request));
		return "order/orderDetails";
	}
	
	// 주문하기 
	@ResponseBody
	@PostMapping(value = "/insertOrder.do", produces = "application/json")
	public void insertOrder(ItemOrderDTO itemOrderDTO, OrderDetailDTO orderDetailDTO, HttpServletRequest request, HttpServletResponse response, Payment payment) throws Exception {
		
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
		
		itemOrderService.insertOrderDetail(orderDetailDTO);
}
	
	
	// 결제 완료
	@GetMapping(value = "/payFinish.do")
	public String payFinish(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request, Payment payment)  throws Exception {
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		
		List<ItemOrderDTO> orderList = itemOrderService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
		model.addAttribute("map", itemOrderService.getUserInfo(request));
		
		itemOrderService.deleteCartByUserNo(request);
		
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
	
  @PostMapping("/payment/cancel")
  private IamportResponse<Payment> cancelPaymentByImpUid(@RequestBody PaymentCancelDTO paymentCancelDTO) throws IamportResponseException, IOException {
      String impUid = paymentCancelDTO.getImp_uid();
      return client.cancelPaymentByImpUid(new CancelData(impUid, true));
  }
	
}