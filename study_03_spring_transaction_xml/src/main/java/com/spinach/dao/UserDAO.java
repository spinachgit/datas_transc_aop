package com.spinach.dao;
import com.spinach.model.User;


public interface UserDAO {
	public void save(User user);
	public void update(User user);
	public User getUser(int id);
}
