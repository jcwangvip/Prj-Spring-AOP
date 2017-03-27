package com.bjpowernode.cglib;

public class TestCglib {
	public static void main(String[] args) throws Exception {
		//TargetDao dao = new TargetDao();
		//dao.insertData();
		
		//通过CGLIB进行功能扩展
		
		//目标对象
		TargetDao dao = new TargetDao();
		
		//扩展对象
		TimeInterceptor methodInterceptor = new TimeInterceptor();
		
		//代理对象		
		Object proxyObject = CGLIBProxy.newProxyInstance(dao, methodInterceptor);
		TargetDao proxyObj = (TargetDao)proxyObject ; //CGLIB代理是基于继承的，所以可以将代理对象转换为父类类型。
		proxyObj.insertData(); //通过调用代理对象方法，CGLIB代理会通过拦截器进行功能扩展。由拦截器执行目标对象方法。
	}
}
