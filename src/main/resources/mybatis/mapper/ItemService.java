package com.hdd.hdeco.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import com.hdd.hdeco.domain.ItemDTO;
import com.hdd.hdeco.domain.UserDTO;


public interface ItemService {
	
  public void getItemList(HttpServletRequest request, Model model);
  public void getItemByNo(int itemNo, Model model);
  
  public ResponseEntity<byte[]> display(int itemNo);
  public ResponseEntity<byte[]> displayDetail(int itemNo);
  
  List<ItemDTO> searchItem(String searchBar);

}
