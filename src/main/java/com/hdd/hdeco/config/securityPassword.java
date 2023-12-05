package com.hdd.hdeco.config;

import com.hdd.hdeco.util.SecurityUtil;

public class securityPassword {

	public static void main(String[] args) {
		
		
		// insert할 회원의 비밀번호 암호화로 만들기(임시 자바파일)
		SecurityUtil securityUtil = new SecurityUtil();
		System.out.println("admin password  = " + securityUtil.getSha256("admin1!"));
		System.out.println("user01 password  = " + securityUtil.getSha256("user01!"));
		System.out.println("user02 password  = " + securityUtil.getSha256("user02@"));
		System.out.println("user03 password  = " + securityUtil.getSha256("user03#"));
		System.out.println("user04 password  = " + securityUtil.getSha256("user04$"));
		System.out.println("user05 password  = " + securityUtil.getSha256("user05%"));
		System.out.println("user06 password  = " + securityUtil.getSha256("user06^"));
		System.out.println("user07 password  = " + securityUtil.getSha256("user07&"));
		System.out.println("user08 password  = " + securityUtil.getSha256("user08*"));
		System.out.println("user09 password  = " + securityUtil.getSha256("user09("));
		System.out.println("user10 password  = " + securityUtil.getSha256("user10!)"));

	}

}
