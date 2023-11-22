package com.hdd.hdeco.domain;

import java.util.Date;

import com.wrapper.spotify.model_objects.specification.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutUserDTO {
	
	private int outUserNo;
	private User userNo;
	private String id;
	private String email;
	private Date joinedAt;
	private Date outAt;

}
