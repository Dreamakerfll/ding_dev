package com.dreamaker.service.user;

import com.dreamaker.domain.user.User;

public interface UserHService {
	
	public int insertUserH(User user);
	
	public int updateAndDeleteUser(User user) throws Exception;
}
