package com.spinach.service;
import java.util.Random;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spinach.model.User;

//Dependency Injection
//Inverse of Control
public class UserServiceTest {

	@Test 
	public void testAdd() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		UserService service = (UserService)ctx.getBean("userService");
		System.out.println(service.getClass());
		User u = new User();
		// 生成随机类
	    Random random = new Random();
		u.setName("spinach"+random.nextInt(20));
		service.add(u);
		ctx.destroy();
	}
	
	@Test 
	public void testUpdate() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		UserService service = (UserService)ctx.getBean("userService");
		System.out.println(service.getClass());
		User user = service.getUser(1);
		user.setName("update user");
		service.updateUser(user);
		ctx.destroy();
		
	}

}
