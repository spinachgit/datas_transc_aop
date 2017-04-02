package com.spinach.javasisst;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spinach.model.User;
import com.spinach.service.UserService;

//Dependency Injection
//Inverse of Control
public class UserServiceTest {

	@Test 
	public void testAdd() throws Exception {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		
		UserService service = (UserService)ctx.getBean("userService");
		System.out.println(service.getClass());
		User u = new User();
		u.setName("spinach01");
		service.add(u);
		
		ctx.destroy();
		
	}

}
