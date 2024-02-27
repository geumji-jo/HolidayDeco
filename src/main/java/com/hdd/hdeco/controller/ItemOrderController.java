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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.mapper.ItemOrderMapper;
import com.hdd.hdeco.service.CartService;
import com.hdd.hdeco.service.ItemOrderService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
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
	private final ItemOrderMapper itemOrderMapper;

	// 아임포트 토큰
	private IamportClient client = new IamportClient("3232467880861681","lSBFzXMaebapZaF0xpcutq4Y2UzDelbeDrNqKS8Xkz8RGKDlnz4eBBJY3PzY2rcjW3VeINQdzO5LpFwH");
  
	// 결제 검증
	@ResponseBody
	@RequestMapping(value = "/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid(Model model, Locale locale, HttpSession session, @PathVariable(value = "imp_uid") String imp_uid) throws IamportResponseException, IOException {
		return client.paymentByImpUid(imp_uid);
	}

	// 장바구니 전체 주문 
	@PostMapping("/orderAll.do")
	public String orderAll(HttpServletRequest request, HttpServletResponse response, Model model) {
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", cartService.getCartList(request, response));
		
		// 장바구니 주문인지 확인하기 위한 basket
		String basket =request.getParameter("basket");
		model.addAttribute("basket",basket);
		
		HttpSession session = request.getSession();//session에 저장하는 basket과 goBuyBasket 이름은 basket으로 통일
		// /orderAll.do에 접근 시 session에 basket값이 있을 경우 세션 값 날려주기
		session.setAttribute("basket", basket);	// 현재페이지는 장바구니 주문에서 넘어오기 때문에 baket만 있고 gobuyBasket은 없음
		if(request.getHeader("referer").contains("/orderAll.do") == false) {
			request.getSession().removeAttribute("basket");
		}
		
		
		return "order/orderDetails";
	}
	
	// 장바구니 선택 주문 
	@PostMapping("/orderSelect.do")
	public String orderSelect(HttpServletRequest request, Model model,ItemOrderDTO itemOrderDTO) {
		
		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		model.addAttribute("cartList", itemOrderService.getSelectItemList(request));
		
		// 장바구니 주문인지 확인하기 위한 basket
		String basket =request.getParameter("basket");
		model.addAttribute("basket",basket);
		
		HttpSession session = request.getSession();	//session에 저장하는 basket과 goBuyBasket 이름은 basket으로 통일
		// /orderSelect.do에 접근 시 session에 basket값이 있을 경우 세션 값 날려주기
		session.setAttribute("basket", basket);	// 현재페이지는 장바구니 주문에서 넘어오기 때문에 baket만 있고 gobuyBasket은 없음
		if(request.getHeader("referer").contains("/orderSelect.do") == false) {
			request.getSession().removeAttribute("basket");
		}
		
			
		return "order/orderDetails";
	}
	
	// 주문하기 
	@ResponseBody
	@PostMapping(value = "/insertOrder.do", produces = "application/json")
	public void insertOrder(ItemOrderDTO itemOrderDTO, OrderDetailDTO orderDetailDTO, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 파리미터로 받아온 장바구니구매(basket)값과 바로구매(goBuyBasket)값
		// 이걸로 구분해서 cart_T있는 데이터를 주문테이블로 넣어주는 것과
		// go_buy_t에 있는 데이터를 주문테이블로 넣을지 구분한다.(insertOrderDetailmapper가 테이블별로 구분되어있어서 이렇게 해야함)
		String basket = request.getParameter("basket");
		String goBuybasket = request.getParameter("goBuyBasket");
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
		
		itemOrderService.insertOrder(itemOrderDTO);
		
		if(basket != null && basket.equals("장바구니구매")) {
		itemOrderService.insertOrderDetail(orderDetailDTO);
		} else if(goBuybasket != null && goBuybasket.equals("바로구매")) {
			itemOrderService.insertGoBuyOrderDetail(orderDetailDTO);
		}
}
	
	
	// 결제 완료
	@PostMapping(value = "/payFinish.do")
	public String payFinish(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request, Payment payment)  throws Exception {
		String basket = request.getParameter("basket");
		String goBuyBasket = request.getParameter("goBuyBasket");
		
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		
		List<ItemOrderDTO> orderList = itemOrderService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
		model.addAttribute("map", itemOrderService.getUserInfo(request));
		
		if(basket.equals("장바구니구매")) {
			itemOrderService.deleteCartByUserNo(request);
			itemOrderMapper.deleteGoBuyItemNo(userNo);
			
		} else if(goBuyBasket.equals("바로구매")) {
			itemOrderMapper.deleteGoBuyItemNo(userNo);
		}
		
		// 혹시 모를 세션지우기
		request.getSession().removeAttribute("basket");
		request.getSession().removeAttribute("goBuyBasket");
		
		
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
	
	@GetMapping("/payFail.do")
	public String payFail(HttpServletRequest request) {
		itemOrderService.deleteOrder(request);
		return "redirect:/order/orderDetail.do?itemOrderNo=" + request.getParameter("itemOrderNo");
	}
	
	
	//바로구매 아이템 go_buy_t db에 저장
	@ResponseBody
	@PostMapping(value = "/addItem.do", produces = "application/json")
	public Map<String, Object> addItem(HttpServletRequest request,CartDTO cartDTO) {
		
		
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId"); 
		int userNo = cartMapper.selectUserNobyId(userId);
		
		
		// 테이블에 저장된 아이템이 있을 경우 지워주기 위함
		List<CartDTO> existingCartItem = cartMapper.selectGoBuyItemList(userNo);
		if(existingCartItem != null) {
			itemOrderMapper.deleteGoBuyItemNo(userNo);
		}
		
		Map<String, Object> map = new HashMap<>();
		map.put("cartDTO", itemOrderService.addGoBuyItem(request));
		return map;
	}

	
//바로구매 디테일 페이지로 이동
	@PostMapping("/orderDetails.html") 
	public String goBuyOrderDetails(Model model,HttpServletRequest request,  HttpServletResponse response) {

		model.addAttribute("orderUser", itemOrderService.getUserInfo(request));
		int userNo = cartService.getUserNo(request);
		
    // 요청 파라미터
		List<CartDTO> cartList = itemOrderService.getGoBuyItemList(request, response);
		model.addAttribute("cartList", cartList);
		model.addAttribute("userNo", cartService.getUserNo(request));
		
		// 바로구매인지 체크해주는 String
		String goBuyBasket = request.getParameter("goBuyBasket");
		model.addAttribute("goBuyBasket",goBuyBasket);
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 장바구니 정보 
		// 장바구니 전체 금액 
		int goBuyPrice = itemOrderService.getGoBuyItemPrice(userNo);
		
		// 장바구니 전체 금액에 따른 배송비 
		// 배송비 (4만원 이상 무료, 4만원 미만 3,000원) 
		int deliveryFee = goBuyPrice > 40000 ? 0 : 3000;
		
		map.put("cartList", cartList);
		map.put("quantity", cartList.size());
		map.put("cartPrice", goBuyPrice);
		map.put("deliveryFee", deliveryFee);
		map.put("orderTotal", goBuyPrice + deliveryFee);
		
		HttpSession session = request.getSession();
		// 뒤로가기 방지용 session "basket"으로 저장
		session.setAttribute("basket", goBuyBasket);
		
		if(request.getHeader("referer").contains("/orderDetails.html") == false) {
			request.getSession().removeAttribute("basket");
		}
		
	  return "order/orderDetails"; 
	  
	  }
}