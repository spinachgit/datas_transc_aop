package com.spinach.aop;

import org.aspectj.lang.ProceedingJoinPoint;

//@Aspect
//@Component
public class LogInterceptor {
	//@Pointcut("execution(public * com.spinach.service..*.add(..))")
	public void myMethod(){
		System.out.println("this my Method!");
	};
	
	//@Before("myMethod()")
	public void beforeMethod() {
		System.out.println("method beforeMethod");
	}
	//@Before("afterMethod()")
	public void afterMethod() {
		System.out.println("method afterMethod");
	}
	public void afterReturning() {
		System.out.println("method afterReturning");
	}
	
	//@Around("myMethod()")
	public void aroundMethod(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("method around start");
		pjp.proceed();
		System.out.println("method around end");
	}
	//@Around("handleException()")
	public void handleException() throws Throwable {
		System.out.println("method handleException start");
	}
}
