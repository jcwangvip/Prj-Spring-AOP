package com.bjpowernode.aop;

import java.util.Date;

/**
 * 扩展对象(执行的代码属于非业务代码)
 */
public class TimePrint {
	/**
	 * 对目标对象进行功能扩展，在执行目标方法前打印日志
	 */
	public void printStartTime(){
		System.out.println("执行目标对象前的系统时间是："+new Date());
	}
	
	/**
	 * 对目标对象进行功能扩展，在执行目标方法后打印日志
	 */
	public void printEndTime(){
		System.out.println("执行目标对象后的系统时间是："+new Date());
	}
}
