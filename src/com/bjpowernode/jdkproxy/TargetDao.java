package com.bjpowernode.jdkproxy;

public class TargetDao implements IDao {
	@Override
	public void insertData() throws Exception {
		System.out.println("insertData..."); //业务逻辑代码
	}
}
