package com.spinach.javasisst;
import java.util.Random;

import org.junit.Test;
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
		// 生成随机类
	    Random random = new Random();
		u.setName("spinach"+random.nextInt(20));
		service.add(u);
		ctx.destroy();
		
	}

}
