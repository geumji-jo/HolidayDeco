package com.hdd.hdeco.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.hdd.hdeco.domain.ItemDTO;


public interface ItemService {
	
  public void getItemList(HttpServletRequest request, Model model);
  public void getItemByNo(Model model,HttpServletRequest request);
  
  List<ItemDTO> searchItem(String query);

}
