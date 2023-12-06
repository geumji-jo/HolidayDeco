package com.hdd.hdeco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hdd.hdeco.domain.ItemDTO;
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

	@GetMapping("/detail.do")
	public String Detail(@RequestParam(value="itemNo") int itemNo, Model model) {
		itemService.getItemByNo(itemNo, model);
		return "item/detail";
	}

	
	@GetMapping("/search.do") public String searchItem(@RequestParam(value="query") String query, Model model) { 
		// 검색 로직 구현 및 결과를 모델에 추가 
		List<ItemDTO>searchResult = itemService.searchItem(query); 
	  model.addAttribute("itemList",searchResult);
	  return "item/list"; 
	 }
}
