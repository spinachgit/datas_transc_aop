package com.spinach.service;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spinach.dao.LogDAO;
import com.spinach.dao.UserDAO;
import com.spinach.model.Log;
import com.spinach.model.User;


@Component("userService")
public class UserService {
	
	private UserDAO userDAO;
	private LogDAO logDAO;
	
	public void init() {
		System.out.println("init");
	}
	
	public User getUser(int id) {
		return null;
	}
	
	@Transactional(readOnly=false)
	public void add(User user) {
			userDAO.save(user);
			Log log = new Log();
			log.setMsg("a user saved!");
			logDAO.save(log);
		
	}
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	@Resource(name="u")
	public void setUserDAO( UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	

	
	public LogDAO getLogDAO() {
		return logDAO;
	}
	
	@Resource
	public void setLogDAO(LogDAO logDAO) {
		this.logDAO = logDAO;
	}

	public void destroy() {
		System.out.println("destroy");
	}
}
