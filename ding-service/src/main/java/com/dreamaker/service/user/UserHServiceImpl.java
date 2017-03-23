package com.dreamaker.service.user;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.dreamaker.dao.user.UserHDao;
import com.dreamaker.domain.user.User;

@Service
public class UserHServiceImpl implements UserHService{
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	private UserHDao userHDao;

	public UserHDao getUserHDao() {
		return userHDao;
	}

	@Resource(name = "userHDao")
	public void setUserHDao(UserHDao userHDao) {
		this.userHDao = userHDao;
	}

	@Override
	@CachePut(value="userid",key="'id_'+'123'")
	public int insertUserH(User user) {
		userHDao.insertUserH(user);
		return 0;
	}

	@Override
	public int updateAndDeleteUser(User user) throws Exception{
		try {
			userHDao.updateUserH(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("向外抛出异常");
		}
		if(user.getId() == 1){
			throw new Exception("向外抛出异常");
		}
		userHDao.deleteUserH(user);
		return 0;
	}


}
