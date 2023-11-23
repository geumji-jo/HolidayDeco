package com.hdd.hdeco.domain;

import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {
	
	private int cartNo;
	private int userNo;
	private String id;
	private Date madeAt;

}
