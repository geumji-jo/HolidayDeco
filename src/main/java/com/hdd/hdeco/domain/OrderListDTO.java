package com.hdd.hdeco.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDTO {
	private String ItemOrderNo;
	private Date orderDate;
	private String name;
	private String mobile;
	private String postcode;
	private String roadAddress;
	private String jibunAddress;
	private String detailAddress;
	private int orderTotal;
	private int deliveryFee;
	private int payNo;
	private int payMethod;
	private int paySuccess;
	private String impUid;
	private int cartNo;

	private int orderDetailNo;
	private int userNo;
	private int itemNo;
	private int quantity;
	private String itemTitle;
	private String itemPrice;
	private String itemMainImg;

	private String delivery;
}
