package com.dreamaker.dao.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dreamaker.domain.user.User;

@Repository(value = "userHDao")  
public class UserHDaoImpl implements UserHDao{

	private SessionFactory sessionFactory;

	@Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    } 
    
	@Override
	public int insertUserH(User user) {
		Session session = sessionFactory.getCurrentSession();
		session.save(user);
		return 0;
	}

}
