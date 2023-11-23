package com.hdd.hdeco.domain;

import java.util.Date;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutUserDTO {
	
	private int outUserNo;
	private int userNo;
	private String id;
	private String email;
	private Date joinedAt;
	private Date outAt;

}
