package com.dreamaker.service.user;

import java.util.List;

import com.dreamaker.domain.user.User;

public interface UserService {
	
	public List<User> selectUserByCondition(User user);
	
	public int insertUser(User user);
	
	public int updateUserByCondition(User user);
	
	public int deleteUser(User user);
}
