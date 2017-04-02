package com.spinach.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;


public class AopTest {

	public static void main(String[] args) {
		// ApplicationContext ctx=new
		// FileSystemXmlApplicationContext("src/main/resources/spring_aop.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring_aop.xml");
		Performer per = (Performer) ctx.getBean("DukePerformer");
		per.perform();
	}
}