package com.hdd.hdeco.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdd.hdeco.service.LikeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/like")
@Controller
public class LikeController {
	
	private final LikeService likeService;

//찜목록 이동 페이지
	@GetMapping("/like.html")
	public String like() {
		return "item/like";
	}
	
	// 찜목록 삽입/삭제
	@ResponseBody
	@GetMapping(value="/like.do", produces="application/json")
	public Map<String, Object> addLike(HttpServletRequest request) {
		return likeService.getLikeByNo(request);
	}
	
	// 찜목록 페이지 삭제
	@GetMapping("/deleteLike.do")
	public String deleteLike(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		int deleteResult = likeService.getLikeDeleteByNo(request);
		redirectAttributes.addFlashAttribute("deleteResult", deleteResult);
		return "redirect:/like/listLike.do";
	}
	
	// 찜목록 리스트
	@GetMapping("/listLike.do")
	public String listLike(HttpServletRequest request, Model model) {
		likeService.getLikeList(request, model);
		return "item/like";
	}
	
}
