package com.hdd.hdeco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCancelDTO {
	 
		private String ItemOrderNo;
		private int userNo;
		private int orderTotal;
		private String imp_uid; // 아임포트 결제번호
	}
