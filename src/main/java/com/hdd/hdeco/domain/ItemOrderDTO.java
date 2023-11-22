package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderDTO {
	
	private int orderNo;
	private int orderTotal;
	private String id;
	private String mobile;
	private String postcode;
	private String jibunAddress;
	private String detailAddress;
	private String name;
	private String itemMainImg;
	private int cartDetailCount;
	private CartDTO cartNo;
	private ItemDTO itemNo;
	private UserDTO userNo;

}
