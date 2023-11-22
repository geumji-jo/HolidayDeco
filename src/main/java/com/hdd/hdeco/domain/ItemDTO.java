package com.hdd.hdeco.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

	private int itemNo;
	private String itemTitle;
	private String itemPrice;
	private String itemMainImg;
	private String itemDetailImg;
	private int itemStock;
	private Date itemWritedAt;
}
