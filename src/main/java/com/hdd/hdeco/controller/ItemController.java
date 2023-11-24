package com.hdd.hdeco.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.service.ItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/item")
@Controller
public class ItemController {
private final ItemService itemService;
	
	@GetMapping("/list.do")
	public String ItemList(HttpServletRequest request, Model model) {
		itemService.getItemList(request, model);
		return "item/list";
	}
	
	@GetMapping("/display.do") 
	public ResponseEntity<byte[]> display(@RequestParam("itemNo") int itemNo) {
		return itemService.display(itemNo);
	}
	
	@GetMapping("/displayDetail.do") 
	public ResponseEntity<byte[]> displayDetail(@RequestParam("itemNo") int itemNo) {
		return itemService.displayDetail(itemNo);
	}
	
	

	/*
	 * @GetMapping("/detail.do") public String Detail(@RequestParam(value="cdNo",
	 * required=false, defaultValue="0") int cdNo, Model model) {
	 * itemService.getCdByNo(cdNo, model); return "shop/detail"; }
	 * 
	 * 
	 * @GetMapping("/getCartDetailList.do") //shopservice를 컨트롤러에서 반환하고 있기 때문에
	 * service에서 getCartDetailList는 void 타입으로 해도 좋다 public String
	 * getCartDetailList(@RequestParam("cartNo") int cartNo , Model model) {
	 * itemService.getCartDetailList(cartNo, model); return "shop/cart"; }
	 * 
	 * 
	 * @ResponseBody
	 * 
	 * @PostMapping(value="/addCartDetail.do", produces="application/json") public
	 * Map<String, Object> addCartDetail(HttpServletRequest request) { return
	 * itemService.addCartDetail(request); }
	 */

}
