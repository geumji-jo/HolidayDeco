package com.hdd.hdeco.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.ItemOrderDTO;
import com.hdd.hdeco.domain.OrderCancelDTO;
import com.hdd.hdeco.domain.OrderListDTO;
import com.hdd.hdeco.service.AdminService;
import com.hdd.hdeco.service.ItemOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

	// field
	private final AdminService adminService;
	private final ItemOrderService itemOrderService;

	// 관리자 페이지 메인 홈
	@GetMapping("/adminPageHome.html")
	public String adminPageHome(ItemOrderDTO itemOrderDTO, Model model) throws Exception {
		List<ItemOrderDTO> orderList = adminService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
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

	// 상품 추가
	@PostMapping("/uploadItem.do")
	public String uploadItem(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes)
			throws Exception {
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

	// 편집 화면
	@GetMapping("/itemEdit.do")
	public String itemEdit(HttpServletRequest request, Model model) {
		adminService.getItemEdit(request, model);
		return "admin/itemEdit";
	}

	// 상품 수정
	@PostMapping("/itemModify.do")
	public String itemModify(MultipartHttpServletRequest multipartRequest, RedirectAttributes redirectAttributes)
			throws Exception {
		adminService.itemModify(multipartRequest);
		return "redirect:/admin/itemManageList.do";
	}

	// 상품 검색
	@GetMapping("/manageSearch.do")
	public String searchItem(@RequestParam(value = "query") String query, Model model) {
		List<ItemDTO> searchResult = adminService.searchItem(query);
		model.addAttribute("itemList", searchResult);
		return "admin/itemManageList";
	}

	// 주문 목록
	@GetMapping(value = "/manageOrderList.do")
	public void getOrderList(ItemOrderDTO itemOrderDTO, Model model) throws Exception {
		List<ItemOrderDTO> orderList = adminService.orderList(itemOrderDTO);
		model.addAttribute("orderList", orderList);
	}

	// 주문 상세 목록
	@GetMapping(value = "/manageOrderView.do")
	public void getOrderList(@RequestParam("n") String itemOrderNo, ItemOrderDTO itemOrderDTO, Model model)
			throws Exception {
		itemOrderDTO.setItemOrderNo(itemOrderNo);

		List<OrderListDTO> orderView = adminService.orderView(itemOrderDTO);

		model.addAttribute("orderView", orderView);
	}

	// 주문 상세 목록 - 상태 변경
	@PostMapping(value = "/manageOrderView.do")
	public String deliveryStatus(@RequestParam("itemOrderNo") String itemOrderNo, @RequestParam("delivery") String delivery, ItemOrderDTO itemOrderDTO) throws Exception {
		adminService.deliveryStatus(itemOrderNo, delivery);
		List<OrderListDTO> orderView = adminService.orderView(itemOrderDTO);

		ItemDTO itemDTO = new ItemDTO();

		for (OrderListDTO i : orderView) {
			itemDTO.setItemNo(i.getItemNo());
			itemDTO.setItemStock(i.getQuantity());
			adminService.UpdateItemStock(itemDTO);
		}
		return "redirect:/admin/manageOrderView.do?n=" + itemOrderNo;
	}

	
	// 주문 취소
	@PostMapping(value= "/orderCancel.do", produces = "application/json")
	public ResponseEntity<String> orderCancel(@RequestBody OrderCancelDTO orderCancelDTO) throws Exception {
	    adminService.insertOrderCancel(orderCancelDTO);
	    System.out.println(orderCancelDTO.toString());
	    if(!"".equals(orderCancelDTO.getImp_uid())) {
	        String token = itemOrderService.getToken();
	        int amount = itemOrderService.paymentInfo(orderCancelDTO.getImp_uid(), token);
	        itemOrderService.payMentCancel(token, orderCancelDTO.getImp_uid(), amount, "관리자 취소");
	    }
	    
	    adminService.orderCancel(orderCancelDTO);

	    // 주문 취소가 성공했음을 응답으로 반환
	    return ResponseEntity.ok().body("{\"message\": \"주문취소완료\"}");
	}
	 

	// 전체 회원리스트보기
	@GetMapping("/totalUserList.html")
	public String totalUserList(HttpServletRequest request, Model model,
			@RequestParam(value = "query", required = false, defaultValue = "") String query) {

		HttpSession session = request.getSession();
		session.setAttribute("query", query);
		System.out.println(session.getAttribute("query"));
		// session에 올라간 recordPerPage,query 값 날려주기
		if (request.getHeader("referer").contains("totalUserList.html") == false) {
			request.getSession().removeAttribute("recordPerPage");
			request.getSession().removeAttribute("query");
		}
		adminService.getTotalUserList(request, model);
		return "admin/totalUserList";
	}

	@GetMapping("/change/record.do")
	public String changeRecord(HttpSession session, HttpServletRequest request,
			@RequestParam(value = "recordPerPage", required = false, defaultValue = "10") int recordPerPage) {
		System.out.println(recordPerPage);
		session.setAttribute("recordPerPage", recordPerPage);
		return "redirect:" + request.getHeader("referer"); // 현재 주소의 이전 주소(Referer)로 이동하시오.
	}

	// 휴면회원리스트보기
	@GetMapping("/sleepUserList.html")
	public String sleepUserList(HttpServletRequest request, Model model,
			@RequestParam(value = "query", required = false, defaultValue = "") String query) {
		HttpSession session = request.getSession();
		session.setAttribute("query", query);
		System.out.println(session.getAttribute("query"));
		// session에 올라간 recordPerPage,query 값 날려주기
		if (request.getHeader("referer").contains("sleepUserList.html") == false) {
			request.getSession().removeAttribute("recordPerPage");
			request.getSession().removeAttribute("query");
		}
		adminService.getSleepUserList(request, model);
		return "admin/sleepUserList";
	}

	// 탈퇴회원리스트보기
	@GetMapping("/outUserList.html")
	public String outUserList(HttpServletRequest request, Model model,
			@RequestParam(value = "query", required = false, defaultValue = "") String query) {
		HttpSession session = request.getSession();
		session.setAttribute("query", query);
		System.out.println(session.getAttribute("query"));
		// session에 올라간 recordPerPage,query 값 날려주기
		if (request.getHeader("referer").contains("outUserList.html") == false) {
			request.getSession().removeAttribute("recordPerPage");
			request.getSession().removeAttribute("query");
		}
		adminService.getOutUserList(request, model);
		return "admin/outUserList";
	}

}
