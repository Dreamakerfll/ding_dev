package com.dreamaker.dao.user;

import com.dreamaker.domain.user.User;

public interface UserHDao {
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int insertUserH(User user);
	
	public int updateUserH(User user);
	
	public int deleteUserH(User user);
}
