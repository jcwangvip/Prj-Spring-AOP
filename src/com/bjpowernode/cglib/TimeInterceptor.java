package com.bjpowernode.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 对目标对象进行功能扩展，打印日志
 */
public class TimeInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object proxyObject, Method method, Object[] args,MethodProxy methodProxy) throws Throwable {
		//执行目标前，打印日志
		System.out.println(method.getName() +"开始执行了");
		
		//执行目标对象方法(通过代理方法来执行)
		Object result = methodProxy.invokeSuper(proxyObject, args); 
		
		//执行目标后，打印日志
		System.out.println(method.getName() +"结束执行了");
		return result;
	}
}
