package com.dreamaker.service.user;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dreamaker.dao.user.UserDao;
import com.dreamaker.domain.user.User;

@Service
public class UserServiceImpl implements UserService{
	
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
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

}
