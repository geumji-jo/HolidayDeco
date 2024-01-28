package com.hdd.hdeco.controller;

import java.io.PrintWriter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.KakaoApproveResponse;
import com.hdd.hdeco.domain.KakaoReadyResponse;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.service.ItemOrderService;
import com.hdd.hdeco.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PaymentController {
	
	private final PaymentService paymentService;
	private final CartMapper cartMapper;
	private final ItemOrderService itemOrderService;
	
  /**
   * 결제요청
   */
	@PostMapping(value="/pay/kakaopayReady.do" ,produces="application/json")
	@ResponseBody
	public Map<String, Object> kakaoPayReady(@RequestParam("orderTotal") String orderTotal,
																					 @RequestParam("itemTitle") String itemTitle,
																					 @RequestParam("quantity") String quantity,
																					 HttpServletRequest request, 
																					 HttpServletResponse response,
																					 HttpSession session,
																					 ItemOrderDTO itemOrderDTO) {
		System.out.println("kakaopayReady 성공..............");
		request.getAttribute(quantity);
		KakaoReadyResponse readyResponse = paymentService.kakaoPayReady(orderTotal,itemTitle,quantity,request,response,itemOrderDTO);
		session.setAttribute("tid", readyResponse.getTid());
		session.setAttribute("itemOrderDTO", itemOrderDTO);
		System.out.println("kakaopayReady tid : " + readyResponse.getTid());
		System.out.println("카카오준비 컨트롤러 itemOrderDTO : " + session.getAttribute("itemOrderDTO"));
		//log.info(".........주문가격 : "+totalAmount);
		
		Map<String, Object> map = new HashMap<>();
		map.put("url", readyResponse.getNext_redirect_pc_url());
		

		return map; //만약 성공시 qr코드가 뜬다 
	}
	
	/*
	 * 결제 승인 요청 큐알 찎을시
	 */
	@GetMapping("/pay/kakaopaySuccess")
	public String kakaoPaySuccess(@RequestParam("pg_token") String pgToken,
																HttpSession session,
																HttpServletRequest request) {
		System.out.println("kakaopaySuccess 성공..............");
		System.out.println("KakapaySuccess pgToken : " + pgToken + "..............");
		String tid = (String) session.getAttribute("tid");
		ItemOrderDTO itemOrderDTO =(ItemOrderDTO) session.getAttribute("itemOrderDTO");
		// 카카오 결제 요청
		KakaoApproveResponse approveResponse = paymentService.kakaoPayApprove(tid, pgToken, itemOrderDTO ,request);
		
		System.out.println("kakaopaySuccess 찐찐성공 : " + approveResponse);
		// 성공한 카트애들을 ORDER 테이블로 이동하기
		

		
		return "redirect:/order/kakaopaySuccess";
	}
	
	@GetMapping("/order/kakaopaySuccess")
	public String kakaoPaySuccessPage(ItemOrderDTO itemOrderDTO, Model model, HttpServletRequest request)throws Exception {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderDTO.setUserNo(userNo);
		List<ItemOrderDTO> orderList = itemOrderService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
		model.addAttribute("map", itemOrderService.getUserInfo(request));
		
		return "/order/kakaopaySuccess";
	}
	
	/*
	 * 	결제 중 취소
	 */
	@GetMapping("/order/kakaopayCancel")
	public void kakaoPayCancel(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("kakaopayCancel 취소..............");
		try {

			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('카카오페이 결제를 취소합니다.');");
			out.println("history.back(-1);");
			out.println("</script>");
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 결제 실패
	 */
	@GetMapping("/order/kakaopayFail")
	public void kakaoPayFail(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("kakaopayFail 실패..............");
		
		try {

			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('카카오페이 결제가 실패하였습니다.');");
			out.println("history.back(-1);");
			out.println("</script>");
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}



};
