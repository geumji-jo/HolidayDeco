package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderListDTO {
	
	private int orderListNo;
	private UserDTO userNo;
	private String name;
	private String mobile;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private int cartDetailCount;
	private int orderTotal;

}
