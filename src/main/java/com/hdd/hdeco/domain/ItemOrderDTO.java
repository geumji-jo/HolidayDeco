package com.hdd.hdeco.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderDTO {
	
	private int ItemOrderNo;
	private Date orderDate;
	private String name;
	private String mobile;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private int orderTotal;
	private String itemMainImg;
	private int deliveryFee;
	private int payNo;
	private int payMethod;
	private int paySuccess;
	private int cartDetailCount;
	private int cartNo;
	private int itemNo;
	private int userNo;

}
