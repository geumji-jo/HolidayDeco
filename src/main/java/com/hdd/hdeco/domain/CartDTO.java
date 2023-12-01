package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	
	private int cartNo;
	private UserDTO userNo;
	private ItemDTO itemNo;
	private int quentity;
	private String itemTitle;
	private String itemPrice;
	private String itemMainImg;

}
