package com.hdd.hdeco.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.service.AdminService;

import lombok.RequiredArgsConstructor;	

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

  //field
  private final AdminService adminService;
  
  
  //관리자페이지메인홈
  @GetMapping("/adminPageHome.html")
  public String adminPageHome() {
    return "admin/adminPageHome";
  }
  
  @GetMapping("/uploadItem.html")
  public String uploadItem() {
    return "admin/uploadItem";
  }
  
  @GetMapping("/itemManageList.do")
	public String itemManageList(HttpServletRequest request, Model model) {
		adminService.getItemManageList(request, model);
		return "admin/itemManageList";
	}

//상품 추가
 @PostMapping("/uploadItem.do")
 public String uploadItem(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
 	 adminService.uploadItem(multipartRequest);
     return "redirect:/admin/itemManageList.do";
 }
	
 // 상품 이미지 
	@GetMapping("/display.do")
	public ResponseEntity<byte[]> display(@RequestParam("itemNo") int itemNo) {
		return adminService.display(itemNo);
	}

	// 상품 디테일 이미지
	@GetMapping("/displayDetail.do")
	public ResponseEntity<byte[]> displayDetail(@RequestParam("itemNo") int itemNo) {
		return adminService.displayDetail(itemNo);
	}
	
	// 상품 삭제
	@PostMapping("/itemDelete.do")
	public String itemDelete(int itemNo, RedirectAttributes redirectAttributes) {
		int removeResult = adminService.deleteItem(itemNo);
		redirectAttributes.addFlashAttribute("removeResult", removeResult);
		return "redirect:/admin/itemManageList.do";
	}
	
	 @GetMapping("/itemEdit.do")
   public String itemEdit(HttpServletRequest request, Model model) {
      adminService.getItemEdit(request, model);
      return "admin/itemEdit";
   }
	
	 // 상품 수정 
	 @PostMapping("/itemModify.do")
	 public String itemModify(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes) throws Exception {
	 	 adminService.itemModify(multipartRequest);
	     return "redirect:/admin/itemManageList.do";
	 }
	 
		@GetMapping("/manageSearch.do") public String searchItem(@RequestParam(value="query") String query, Model model) { 
			// 검색 로직 구현 및 결과를 모델에 추가 
			List<ItemDTO>searchResult = adminService.searchItem(query); 
		  model.addAttribute("itemList",searchResult);
		  return "admin/itemManageList"; 
		 }
  
 }
  
	  
	