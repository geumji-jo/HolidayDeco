package com.hdd.hdeco.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

public interface LikeService {
	
	public Map<String, Object> getLikeByNo(HttpServletRequest request);
	public int getLikeDeleteByNo (HttpServletRequest request);
	public void getLikeList (HttpServletRequest request, Model model);
	
}
