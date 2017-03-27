package com.bjpowernode.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 对系统的业务方法进行功能扩展：在执行方法前，后打印系统日志
 */
public class TimeInvocationHandler implements InvocationHandler {

	private Object targetObject ; //目标对象
	
	public TimeInvocationHandler(Object targetObject){
		this.targetObject = targetObject ;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//执行目标前，打印日志
		System.out.println(targetObject.getClass().getName() +" - " + method.getName() +"开始执行了");
		
		//执行目标对象方法
		Object result = method.invoke(targetObject);
		
		//执行目标后，打印日志
		System.out.println(targetObject.getClass().getName() +" - " + method.getName() +"结束执行了");
		return result;
	}
}
