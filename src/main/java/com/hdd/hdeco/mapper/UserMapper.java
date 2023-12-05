package com.hdd.hdeco.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hdd.hdeco.domain.OutUserDTO;
import com.hdd.hdeco.domain.SleepUserDTO;
import com.hdd.hdeco.domain.UserDTO;

@Mapper
public interface UserMapper {
	  public UserDTO selectUserById(String id);
	  public SleepUserDTO selectSleepUserById(String id);
	  public OutUserDTO selectOutUserById(String id);
	  public UserDTO selectUserByEmail(String email);
	  public SleepUserDTO selectSleepUserByEmail(String email);
	  public OutUserDTO selectOutUserByEmail(String email);
	  public int insertUser(UserDTO userDTO);
	  public UserDTO selectUserByUserDTO(UserDTO userDTO);
	  public int insertUserAccess(String id);
	  public int updateUserAccess(String id);
	  public int insertAutologin(UserDTO userDTO);
	  public int deleteAutologin(String id);
	  public UserDTO selectAutologin(String autologinId);
	  public int insertOutUser(OutUserDTO outUserDTO);
	  public int deleteUser(String id);

		public int insertSleepUser();
	  public int deleteUserForSleep();
	  public int insertRestoreUser(String id);
	  public int deleteSleepUser(String id);
	  public int updateUserPassword(UserDTO userDTO);
	  public int updateUserEmail(UserDTO userDTO);
	  public int updateUserInfo(UserDTO userDTO);
	  public UserDTO selectFindUserId(UserDTO userDTO);
	  public String selectUserPwByUserDTO(UserDTO userDTO);
	  public String selectUserPwCheck(UserDTO userDTO);
}