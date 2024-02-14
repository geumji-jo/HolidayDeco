package com.hdd.hdeco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	
	@GetMapping("/detail.do")
	public String detail(HttpServletRequest request, Model model) {
	    int itemNo = Integer.parseInt(request.getParameter("itemNo"));
	    itemService.getItemByNo(model, request);
	    return "item/detail";
	}

	
	@GetMapping("/search.do") public String searchItem(@RequestParam(value="query") String query, Model model) { 
		// 검색 로직 구현 및 결과를 모델에 추가 
		List<ItemDTO>searchResult = itemService.searchItem(query); 
	  model.addAttribute("itemList",searchResult);
	  return "item/list"; 
	 }
}
