package com.hdd.hdeco.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.KakaoApproveResponse;
import com.hdd.hdeco.domain.KakaoReadyResponse;

public interface PaymentService {
	public KakaoReadyResponse kakaoPayReady(String orderTotal, String itemTitle,String quantity, HttpServletRequest request, HttpServletResponse response,ItemOrderDTO itemOrderDTO);
	public KakaoApproveResponse kakaoPayApprove(String tid, String pgToken, ItemOrderDTO itemOrderDTO,HttpServletRequest request);
}
