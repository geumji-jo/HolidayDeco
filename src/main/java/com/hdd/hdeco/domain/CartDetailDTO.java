package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {
	
	private int cartDetailNo;
	private int cartDetailCount;
	private int cartDetailCheck;
	private String itemTitle;
	private String itemPrice;
	private CartDTO cartNo;
	private ItemDTO itemNo;
	private UserDTO userNo;

}
