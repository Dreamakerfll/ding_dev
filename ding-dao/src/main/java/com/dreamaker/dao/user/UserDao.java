package com.dreamaker.dao.user;

import java.util.List;

import com.dreamaker.domain.user.User;

public interface UserDao {
	
	/**
	 * 根据条件查找用户
	 * @param user
	 * @return
	 */
	public List<User> selectUserByCondition(User user);
	
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int insertUser(User user);
	
	/**
	 * 根据条件更新用户
	 * @param user
	 * @return
	 */
	public int updateUserByCondition(User user);
	
	/**
	 * 删除用户
	 * @param user
	 * @return
	 */
	public int deleteUser(User user);
}
