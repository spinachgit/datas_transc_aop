package com.spinach.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import com.spinach.dao.UserDAO;
import com.spinach.model.User;

@Component("u") 
public class UserDAOImpl implements UserDAO {

	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	@Resource
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void save(User user) {
			Session s = sessionFactory.getCurrentSession();
			s.save(user);
	}

	public void update(User user) {
		Session s = sessionFactory.getCurrentSession();
		s.saveOrUpdate(user);
	}

	public User getUser(int id) {
		String sql = "select u.id,u.name from User u where u.id = "+id ;
		Session session = sessionFactory.getCurrentSession();
		Query  query = session.createQuery(sql);
		List list = query.list();
		List<User> resultList = new ArrayList<User>();
		Object[] row = null;
		User user = null;
		row = (Object[])list.get(0);
		user = new User();
		user.setId(Integer.valueOf(row[0].toString()));
		user.setName(row[1].toString());
		resultList.add(user);
		return user;
	}

}
