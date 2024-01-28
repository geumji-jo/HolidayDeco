package com.hdd.hdeco.service;

import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.KakaoApproveResponse;
import com.hdd.hdeco.domain.KakaoReadyResponse;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.mapper.ItemOrderMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {


	private final ItemOrderService itemOrderService;
	private final ItemOrderMapper itemOrderMapper;
	private final CartMapper cartMapper;
	
	
	
	
	// 주문한 상품의 전체 갯수 구하기
	 public static int concatenateAndSum(String numberWithCommas) {
     // 쉼표 제거 후 숫자를 문자열로 변환
     String numberStr = numberWithCommas.replaceAll(",", "");

     // 각 자리 숫자 합산
     int totalSum = 0;
     for (int i = 0; i < numberStr.length(); i++) {
         // 각 자리의 문자를 정수로 변환하여 합산
         totalSum += Character.getNumericValue(numberStr.charAt(i));
     }

     return totalSum;
 }

	

	// 결제요청 준비
	@Override
	public KakaoReadyResponse kakaoPayReady(String orderTotal, String itemTitle,String quantity, HttpServletRequest request, HttpServletResponse response,ItemOrderDTO itemOrderDTO) {


		String orderId = "3333333";
		int total = Integer.parseInt(orderTotal);
		System.out.println("orderTotal............" + orderTotal);
		System.out.println("quantity............" + String.valueOf(quantity));
		int quantityTotalCount = concatenateAndSum(String.valueOf(quantity));
		 System.out.println("quantityTotalCount............" + quantityTotalCount); 
		/*
		 * 카카오 페이가 요구하는 결제요청 request 담기
		 * */
		MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		parameters.add("cid", "TC0ONETIME");
		parameters.add("partner_order_id", orderId);
		parameters.add("partner_user_id", "HolidayDeco");
		parameters.add("item_name", itemTitle); 
		parameters.add("quantity", String.valueOf(quantityTotalCount)); // String.valueOf(carts.size()) 총개수  
		parameters.add("total_amount", String.valueOf(total)); // int total 받을 시에는 total대신 String.valueOf(total)
		parameters.add("tax_free_amount", "0");
		parameters.add("approval_url", "http://localhost:8080/pay/kakaopaySuccess"); // 결제승인시 넘어갈 url
		parameters.add("cancel_url", "http://localhost:8080/order/kakaopayCancel"); // 결제취소시 넘어갈 url
		parameters.add("fail_url", "http://localhost:8080/order/kakaopayFail"); // 결제 실패시 넘어갈 url

		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
		/*
		 * 외부 url 요청 통로 열기
		 * */
		RestTemplate template = new RestTemplate();
		String url = "https://kapi.kakao.com/v1/payment/ready";
		
		
		/*
		 * template으로 값을 보내고 받아온 ReadyResponse값 readyResponse에 저장.
		 */
		KakaoReadyResponse readyResponse = template.postForObject(url, requestEntity, KakaoReadyResponse.class);
	
		System.out.println("결제준비 tid : " + readyResponse.getTid());
		System.out.println("결제준비 리다이렉트 경로 : " + readyResponse.getNext_redirect_pc_url());
		System.out.println("결제준비 전제정보 : " + readyResponse);
		
		try {
			if(readyResponse.getTid() != null) {
			
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

   		String name = request.getParameter("name");
   		String mobile = request.getParameter("mobile");
   		String postcode = request.getParameter("postcode");
   		String roadAddress = request.getParameter("roadAddress");
   		int payMethod =Integer.parseInt(request.getParameter("payMethod"));
   		int userNo = Integer.parseInt(request.getParameter("userNo"));
   		
   		//itemOrderDTO에 결제정보 담아두기
   		
   		itemOrderDTO.setItemOrderNo(itemOrderNo);
   		itemOrderDTO.setName(name);
   		itemOrderDTO.setMobile(mobile);
   		itemOrderDTO.setPostcode(postcode);
   		itemOrderDTO.setRoadAddress(roadAddress);
   		itemOrderDTO.setPayMethod(payMethod);
   		itemOrderDTO.setUserNo(userNo);
   		
   		System.out.println(itemOrderDTO);

		}
   		
	} catch (Exception e) {
		e.printStackTrace();
	}

		
		return readyResponse;
	}


	
   /* 
    * 결제 승인요청
    */
		@Override
   public KakaoApproveResponse kakaoPayApprove(String tid, String pgToken, ItemOrderDTO itemOrderDTO, HttpServletRequest request) {
		System.out.println(pgToken + "........service 승인요청 pgToken");
		System.out.println(tid + "........service 승인요청 tid");
		System.out.println(itemOrderDTO + "........큐알코드 찍고 최종 itemOrderDTO");

    String orderId = "3333333";

      MultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
      parameters.add("cid", "TC0ONETIME");
      parameters.add("tid", tid);
      parameters.add("partner_order_id", orderId); // 주문명
      parameters.add("partner_user_id", "HolidayDeco");//회사
      parameters.add("pg_token", pgToken);
      
      // parameter값과 header 담기 건드릴 필요없음
      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeader());
      RestTemplate template = new RestTemplate();

     String url = "https://kapi.kakao.com/v1/payment/approve";
     KakaoApproveResponse approveResponse = template.postForObject(url, requestEntity, KakaoApproveResponse.class);
         System.out.println("결제승인 응답객체: " + approveResponse);
     		
     		if(approveResponse != null) {
     			try {
            // 1. 아이템 주문정보 itemOrderDTO에 저장히기
     				itemOrderService.insertOrder(itemOrderDTO);
     				
     				// 2. 아이템 주문저장 orderDetailDTO에도 저장해주기
     				OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
     				orderDetailDTO.setItemOrderNo(itemOrderDTO.getItemOrderNo());
     				itemOrderService.insertOrderDetail(orderDetailDTO);
     				
     				
            HttpSession session = request.getSession();
            
            // 3. 아이템 주문저장 후 카트 비워주기
        		String userId = (String)session.getAttribute("loginId");
        		int userNo = cartMapper.selectUserNobyId(userId);
        		itemOrderMapper.deleteCartByUserNo(userNo);
        		
        } catch (Exception e) {
            e.printStackTrace();
        }
     		}
     		
         return approveResponse;
   }
   

   @Value("${kakaopay.Authorization}")
   private String auth;
   
   private HttpHeaders getHeader() {
      System.out.println("가져오기 : ........... " + auth);
      HttpHeaders headers = new HttpHeaders();
      headers.set("Authorization", auth);
      headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
      
      return headers;
   }
}
