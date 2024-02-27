package com.hdd.hdeco.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemOrderDTO {
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
	private int payMethod;
  private int quantity;
  private String delivery;
	private String imp_uid;
	private int userNo;


}
