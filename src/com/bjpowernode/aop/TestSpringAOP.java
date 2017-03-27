package com.bjpowernode.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringAOP {

	public static void main(String[] args) throws Exception {
		ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		//采用cglib代理(没有接口情况下)
		/*Object proxyObject = ac.getBean("targetDao"); 
		TargetDao proxyObj = (TargetDao)proxyObject; //因为目标对象没有接口，所以框架会采用cglib进行代理
		proxyObj.insertData(); */
		
		//采用JDK动态代理(有接口情况下)
		IDao proxyObject = (IDao)ac.getBean("targetDao");
		proxyObject.insertData();
	}

}
