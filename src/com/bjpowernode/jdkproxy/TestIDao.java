package com.bjpowernode.jdkproxy;

import java.lang.reflect.Proxy;

public class TestIDao {
	public static void main(String[] args) throws Exception {
		//IDao dao = new TargetDao();
		//dao.insertData();
		
		//JDK动态代理,进行日志打印功能扩展
		//目标对象
		IDao dao = new TargetDao();
		
		//扩展对象
		TimeInvocationHandler handler = new TimeInvocationHandler(dao);
		
		//代理对象
		Object proxyObject = Proxy.newProxyInstance(dao.getClass().getClassLoader(), new Class[]{IDao.class}, handler);
		IDao proxyObj = (IDao)proxyObject;
		proxyObj.insertData(); //当调用代理对象的方法，代理会将功能扩展委托给扩展对象，然后由扩展对象执行目标对象方法。
	}
}
