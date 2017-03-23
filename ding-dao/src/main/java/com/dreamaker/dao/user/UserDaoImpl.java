package com.dreamaker.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.dreamaker.domain.user.User;

@Repository(value="userDao")
public class UserDaoImpl implements UserDao {

	@Override
	public List<User> selectUserByCondition(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateUserByCondition(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteUser(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

}
