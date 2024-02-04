package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderCancelDTO;
import com.hdd.hdeco.domain.OrderListDTO;

public interface AdminService {

	// 상품 관리
	public void getItemByNo(Model model, HttpServletRequest request);
	public void getItemManageList(HttpServletRequest request, Model model);
	public int uploadItem(MultipartHttpServletRequest multipartRequest) throws Exception;
	public ResponseEntity<byte[]> display(int itemNo);
	public ResponseEntity<byte[]> displayDetail(int itemNo);
	public int deleteItem(int itemNo);
	public void getItemEdit(HttpServletRequest request, Model model);
	public int itemModify(MultipartHttpServletRequest multipartRequest) throws Exception;
	List<ItemDTO> searchItem(String query);
	public List<ItemOrderDTO> orderList(ItemOrderDTO itemOrderDTO) throws Exception;
	public List<OrderListDTO> orderView(ItemOrderDTO itemOrderDTO) throws Exception;
	public void deliveryStatus(String itemOrderNo, String delivery);
	public void UpdateItemStock(ItemDTO itemDTO);

  //주문 취소
	public void orderCancel(OrderCancelDTO orderCancelDTO);
	public void insertOrderCancel(OrderCancelDTO orderCancelDTO) throws Exception;
	
	// 회원관리
	public void getTotalUserList(HttpServletRequest request, Model model);
	public void getSleepUserList(HttpServletRequest request, Model model);
	public void getOutUserList(HttpServletRequest request, Model model);
}