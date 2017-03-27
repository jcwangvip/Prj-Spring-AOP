package com.bjpowernode.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

/**
 * 定义工具类，通过CGLIB来产生代理对象
 */
public abstract class CGLIBProxy {
	
	/**
	 * 通过CGLIB产生代理对象
	 * @param targetObject 目标对象
	 * @param methodInterceptor 扩展对象
	 * @return 代理对象
	 * @throws Exception
	 */
	public static Object newProxyInstance(Object targetObject,MethodInterceptor methodInterceptor) throws Exception{ 
		
		//表示字节码增强工具，在目标对象的基础上增加扩展功能。
		Enhancer eh = new Enhancer();
		eh.setSuperclass(targetObject.getClass()); //设置父类
		eh.setCallback(methodInterceptor); //设置扩展对象
		return eh.create(); //创建代理对象
	}
}
