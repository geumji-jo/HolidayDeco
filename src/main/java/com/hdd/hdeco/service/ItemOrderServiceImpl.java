
package com.hdd.hdeco.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hdd.hdeco.domain.CartDTO;
import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderDetailDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.domain.UserDTO;
import com.hdd.hdeco.mapper.CartMapper;
import com.hdd.hdeco.mapper.ItemOrderMapper;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemOrderServiceImpl implements ItemOrderService {

	private final ItemOrderMapper itemOrderMapper;
	private final CartMapper cartMapper;

	// 주문하기 : 주문 후 주문 정보 return
	@Override
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO) {
		itemOrderMapper.insertOrder(itemOrderDTO);
		return itemOrderMapper.selectUserOrder(itemOrderDTO.getUserNo());
	}
  // 카트 정보 orderDetail에 저장 
	@Override
	public void insertOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
		itemOrderMapper.insertOrderDetail(orderDetailDTO);
	}

	// user정보 조회 : 아이디를 통해 userNo 확인
	@Override
	public UserDTO getUserInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");

		int userNo = cartMapper.selectUserNobyId(userId);

		return itemOrderMapper.getUserByUserNo(userNo);
	}

	@Override
	public List<CartDTO> getSelectItemList(HttpServletRequest request) {
		String[] items = request.getParameter("selectedItems").split(",");
		List<CartDTO> list = new ArrayList<>();
		for (String itemNo : items) {
			list.add(itemOrderMapper.getItemByNo(Integer.parseInt(itemNo)));
		}
		return list;
	}

	@Override
	public ItemDTO getItem(HttpServletRequest request) {
		int itemNo = Integer.parseInt(request.getParameter("itemNo"));
		return itemOrderMapper.getFromItem(itemNo);
	}

	// 결제 성공 후 카트 삭제
	@Override
	public void deleteCartByUserNo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("loginId");
		int userNo = cartMapper.selectUserNobyId(userId);
		itemOrderMapper.deleteCartByUserNo(userNo);
	}

	@Override
	public void orderInfo(ItemOrderDTO itemOrderDTO) throws Exception {
		itemOrderMapper.orderInfo(itemOrderDTO);
	}

	@Override
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception {
		return itemOrderMapper.orderList(itemOrderDTO);
	}

	@Override
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception {
		return itemOrderMapper.orderView(itemOrderDTO);
	}

	// 결제실패
	@Override
	public void deleteOrder(HttpServletRequest request) {
		String itemOrderNo = request.getParameter("itemOrderNo");
		itemOrderMapper.deleteOrder(itemOrderNo);
	}

	
	  @Value("${imp_key}") private String impKey;
	 
	  @Value("${imp_secret}") private String impSecret;
	 
	@Data
	private class Response{
		private PaymentInfo response;
	}
	
	@Data
	private class PaymentInfo{
		private int amount;
	}
	
	// 아임포트 토큰 
  @Override
	public String getToken() throws IOException {

		HttpsURLConnection conn = null;

		URL url = new URL("https://api.iamport.kr/users/getToken");

		conn = (HttpsURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);
		JsonObject json = new JsonObject();

		json.addProperty("imp_key", impKey);
		json.addProperty("imp_secret", impSecret);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		
		bw.write(json.toString());
		bw.flush();
		bw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

		Gson gson = new Gson();

		String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
		
		System.out.println(response);

		String token = gson.fromJson(response, Map.class).get("access_token").toString();

		br.close();
		conn.disconnect();

		return token;
	}
  
  // 아임포트 결제 정보 
  @Override
	public int paymentInfo(String imp_uid, String access_token) throws IOException {

	    HttpsURLConnection conn = null;
	    
	    URL url = new URL("https://api.iamport.kr/payments/" + imp_uid);
	 
	    conn = (HttpsURLConnection) url.openConnection();
	 
	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", access_token);
	    conn.setDoOutput(true);
	 
	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	    
	    Gson gson = new Gson();
	    
	    Response response = gson.fromJson(br.readLine(), Response.class);
	    
	    br.close();
	    conn.disconnect();
	    
	    return response.getResponse().getAmount();
	}
	
	
	// 아임포트 결제 취소 
	public void payMentCancel(String access_token, String imp_uid, int amount, String reason) throws IOException  {
		System.out.println("결제 취소");
		
		System.out.println(access_token);
		
		System.out.println(imp_uid);
		
		HttpsURLConnection conn = null;
		URL url = new URL("https://api.iamport.kr/payments/cancel");
 
		conn = (HttpsURLConnection) url.openConnection();
 
		conn.setRequestMethod("POST");
 
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", access_token);
 
		conn.setDoOutput(true);
		
		JsonObject json = new JsonObject();
 
		json.addProperty("reason", reason);
		json.addProperty("imp_uid", imp_uid);
		json.addProperty("amount", amount);
		json.addProperty("checksum", amount);
 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
 
		bw.write(json.toString());
		bw.flush();
		bw.close();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
 
		br.close();
		conn.disconnect();
	}
}


