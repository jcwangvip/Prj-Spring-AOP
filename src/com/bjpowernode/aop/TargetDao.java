package com.bjpowernode.aop;

/**
 * 业务对象
 * 目标对象中的所有关注的方法，称为连接点。
 * 符合切入点表达式的方法，称为切入点。（切入点可以看做连接点的子集）
 */
public class TargetDao implements IDao{
	
	//业务代码
	public void insertData() throws Exception { 
		System.out.println("insertData..."); //业务逻辑代码
	}
}
