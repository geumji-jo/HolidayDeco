
package com.hdd.hdeco.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemOrderServiceImpl implements ItemOrderService {

	private final ItemOrderMapper itemOrderMapper;
	private final CartMapper cartMapper;

	private IamportClient client = new IamportClient("3232467880861681",
			"lSBFzXMaebapZaF0xpcutq4Y2UzDelbeDrNqKS8Xkz8RGKDlnz4eBBJY3PzY2rcjW3VeINQdzO5LpFwH");

	// 주문하기 : 주문 후 주문 정보 return
	@Override
	public ItemOrderDTO insertOrder(ItemOrderDTO itemOrderDTO) {
		itemOrderMapper.insertOrder(itemOrderDTO);
		return itemOrderMapper.selectUserOrder(itemOrderDTO.getUserNo());
	}

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
		int itemOrderNo = Integer.parseInt(request.getParameter("itemOrderNo"));
		itemOrderMapper.deleteOrder(itemOrderNo);
	}
	/*
	 * @Override public void refundRequest(String access_token, String merchant_uid,
	 * String reason) throws IOException { URL url = new
	 * URL("https://api.iamport.kr/payments/cancel"); HttpsURLConnection conn =
	 * (HttpsURLConnection) url.openConnection();
	 * 
	 * // 요청 방식을 POST로 설정 conn.setRequestMethod("POST");
	 * 
	 * // 요청의 Content-Type, Accept, Authorization 헤더 설정
	 * conn.setRequestProperty("Content-type", "application/json");
	 * conn.setRequestProperty("Accept", "application/json");
	 * conn.setRequestProperty("Authorization", access_token);
	 * 
	 * // 해당 연결을 출력 스트림(요청)으로 사용 conn.setDoOutput(true);
	 * 
	 * // JSON 객체에 해당 API가 필요로하는 데이터 추가. JsonObject json = new JsonObject();
	 * json.addProperty("merchant_uid", merchant_uid); json.addProperty("reason",
	 * reason);
	 * 
	 * // 출력 스트림으로 해당 conn에 요청 BufferedWriter bw = new BufferedWriter(new
	 * OutputStreamWriter(conn.getOutputStream())); bw.write(json.toString());
	 * bw.flush(); bw.close();
	 * 
	 * // 입력 스트림으로 conn 요청에 대한 응답 반환 BufferedReader br = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())); br.close(); conn.disconnect();
	 * 
	 * System.out.println("결제 취소 완료 : 주문 번호 {} :" + merchant_uid);
	 * 
	 * String responseJson = new BufferedReader(new
	 * InputStreamReader(conn.getInputStream())) .lines()
	 * .collect(Collectors.joining("\n"));
	 * 
	 * System.out.println("응답 본문: " + responseJson);
	 * 
	 * JsonObject jsonResponse =
	 * JsonParser.parseString(responseJson).getAsJsonObject(); String resultCode =
	 * jsonResponse.get("code").getAsString(); String resultMessage =
	 * jsonResponse.get("message").getAsString();
	 * 
	 * System.out.println("결과 코드 = " + resultCode); System.out.println("결과 메시지 = " +
	 * resultMessage); }
	 */

	// ---------------------환불, 결제 토큰생성
	@Value("imp_key")
	private String imp_key;

	@Value("imp_secret")
	private String imp_secret;

	@Override
	public String getToken() throws Exception {

		HttpsURLConnection conn = null;
		URL url = new URL("https://api.iamport.kr/users/getToken");

		conn = (HttpsURLConnection) url.openConnection();

		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setDoOutput(true);
		JsonObject json = new JsonObject();

		json.addProperty("imp_key", imp_key);
		json.addProperty("imp_secret", imp_secret);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

		bw.write(json.toString());
		bw.flush();
		bw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

		Gson gson = new Gson();

		String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();

		String token = gson.fromJson(response, Map.class).get("access_token").toString();

		br.close();
		conn.disconnect();

		return token;
	}

	// 결제 정보 불러오기
	@Override
	public String paymentInfo(String imp_uid, String access_token, ItemOrderDTO itemOrderDTO) throws IOException, ParseException {
	    HttpsURLConnection conn = null;

	    URL url = new URL("https://api.iamport.kr/payments/" + imp_uid);

	    conn = (HttpsURLConnection) url.openConnection();

	    conn.setRequestMethod("GET");
	    conn.setRequestProperty("Authorization", access_token);
	    conn.setDoOutput(true);

	    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));

	    JSONParser parser = new JSONParser();

	    JSONObject p = (JSONObject) parser.parse(br.readLine());

	    String response = p.get("response").toString();

	    p = (JSONObject) parser.parse(response);

	    String amount = p.get("amount").toString();
	    return amount;
	}

	@Override
	public void payMentCancle(String access_token, String imp_uid, String amount, String reason)
			throws IOException, ParseException {
		System.out.println("imp_uid = " + imp_uid);
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

		System.out.println("check 1 : " + imp_uid);
		System.out.println("check 2 : " + amount);
		System.out.println(reason);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));

		bw.write(json.toString());
		bw.flush();
		bw.close();

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
	}
	/*
	 * @Override public void updateImpUid(String itemOrderNo, String imp_uid) {
	 * Map<String, String> map = new HashMap<>(); map.put("itemOrderNo",
	 * itemOrderNo); map.put("imp_uid", imp_uid); itemOrderMapper.updateImpUid(map);
	 * 
	 * }
	 */
}
