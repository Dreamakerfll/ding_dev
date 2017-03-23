package com.dreamaker.service.user;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dreamaker.dao.user.UserDao;
import com.dreamaker.domain.user.User;

@Service
public class UserServiceImpl implements UserService{
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource(name = "userDao")
	private UserDao userDao;
	
	@Cacheable(key="'id_'+#user.getId()", value="userid")
	public List<User> selectUserByCondition(User user) {
		return userDao.selectUserByCondition(user);
	}

	public int insertUser(User user) {
		return userDao.insertUser(user);
	}

	@CachePut(value="userid",key="'id_'+#user.getId()")// 更新user 缓存  
	public int updateUserByCondition(User user) {
		return userDao.updateUserByCondition(user);
	}

	@CacheEvict(value="userid",key="'id_'+#user.getId()")
	public int deleteUser(User user) {
		return userDao.deleteUser(user);
	}

	@Override
	public boolean updateAndDeleteUser(User user) throws Exception{
		try {
			userDao.updateUserByCondition(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception("向外抛出异常");
		}
		if(user.getId() == 1){
			throw new RuntimeException();
		}
		userDao.deleteUser(user);
		return true;
	}


}
