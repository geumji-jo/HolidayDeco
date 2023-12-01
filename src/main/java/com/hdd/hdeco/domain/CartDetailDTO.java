package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetailDTO {
	
	private int cartDetailNo;
	private ItemOrderDTO orderNo;
	private int quantity;
	private int orderTotal;
	private ItemDTO itemNo;

}
