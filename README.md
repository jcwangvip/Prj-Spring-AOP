# Prj-Spring-AOP

	Spring框架的AOP (Aspect Oriented Programming) 面向方面编程
	
	AOP 是一种编程思想。主要是以横向的编程方式解决业务流程中重复性出现的问题。
	
	从实现上，一般采用JDK动态代理。
	
	JDK动态代理是基于接口的，如果一个目标类，没有实现的接口，无法进行代理。
	
	那么可以采用其他的代理方式： CGLIB代理，基于继承的。
	
	CGLIB是一个代理组件，专门用于扩展业务功能，只不过是基于继承的。
	
	1) JDK动态代理
	
		1-1) 定义接口
		
			public interface IDao {
				public abstract void insertData() throws Exception ;
			}
			
		1-2) 定义实现类
		
			public class TargetDao implements IDao {
				@Override
				public void insertData() throws Exception {
					System.out.println("insertData..."); //业务逻辑代码
				}
			}	
			
		1-3) 测试业务代码
		
			public class TestIDao {
				public static void main(String[] args) throws Exception {
					IDao dao = new TargetDao();
					dao.insertData();
				}
			}
		
		1-4) 用户提出新的需求，需要在执行业务方法时，打印系统的日志。
		
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
			
		1-5) 重新测试
		
			//JDK动态代理,进行日志打印功能扩展
			//目标对象
			IDao dao = new TargetDao();
			
			//扩展对象
			TimeInvocationHandler handler = new TimeInvocationHandler(dao);
			
			//代理对象
			Object proxyObject = Proxy.newProxyInstance(dao.getClass().getClassLoader(), new Class[]{IDao.class}, handler);
			IDao proxyObj = (IDao)proxyObject;
			proxyObj.insertData(); //当调用代理对象的方法，代理会将功能扩展委托给扩展对象，然后由扩展对象执行目标对象方法。	
			
			
	2) CGLIB代理
	
		2-1) 定义业务类
		
			public class TargetDao {
				public void insertData() throws Exception {
					System.out.println("insertData..."); //业务逻辑代码
				}
			}
			
		2-2) 测试
			public class TestCglib {
				public static void main(String[] args) throws Exception {
					TargetDao dao = new TargetDao();
					dao.insertData();
				}
			}	
			
		2-3) 用户提出新的需求，需要在执行业务方法时，打印系统的日志。	
		
			因为目标对象没有接口，所以无法使用JDK动态代理进行功能扩展。
			
			那么可以使用CGLIB进行代理。基于继承的。
			
			2-3-1) 引入cglib组件的jar包
			
					spring-framework-2.5.6\lib\cglib\cglib-nodep-2.1_3.jar
					
			2-3-2) 定义扩展类		

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
					
			2-3-3) 自定义工具类，来产生代理对象
			
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
					
			2-3-4) 重新测试
			
				//目标对象
				TargetDao dao = new TargetDao();
				
				//扩展对象
				TimeInterceptor methodInterceptor = new TimeInterceptor();
				
				//代理对象		
				Object proxyObject = CGLIBProxy.newProxyInstance(dao, methodInterceptor);
				TargetDao proxyObj = (TargetDao)proxyObject ; //CGLIB代理是基于继承的，所以可以将代理对象转换为父类类型。
				proxyObj.insertData(); //通过调用代理对象方法，CGLIB代理会通过拦截器进行功能扩展。由拦截器执行目标对象方法。		
			
			
	3) Spring框架中应用AOP功能
	
		2-1) Spring集成AspectJ框架（专门做AOP应用开发），需要增加额外的jar包
		
				spring-framework-2.5.6\lib\aspectj\aspectjrt.jar
				spring-framework-2.5.6\lib\aspectj\aspectjweaver.jar
				
				cglib-nodep-2.1_3.jar
				
		2-2) 在Spring配置文件中增加AOP的命名空间和约束文件		
			<beans ...
				xmlns:aop="http://www.springframework.org/schema/aop"
				...
				xsi:schemaLocation="
					...
					http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd
					>
					
		2-3) 启用AOP功能
			默认框架不支持AOP功能，需要启用AOP功能。
			
			<!-- 启动AOP功能 -->
			<aop:aspectj-autoproxy />					
			
		2-4) 定义业务类
		
			public class TargetDao {
				public void insertData() throws Exception {
					System.out.println("insertData..."); //业务逻辑代码
				}
			}	
			
		2-5) 定义扩展类
		
				/**
				 * 扩展对象
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
				
		2-6) 声明目标对象和扩展对象		
			
			<!-- 声明目标对象 -->
			<bean id="targetDao" class="com.bjpowernode.aop.TargetDao"></bean>
			
			<!-- 声明扩展对象 -->
			<bean id="timePrint" class="com.bjpowernode.aop.TimePrint"></bean>
			
		2-7) 声明目标对象和扩展对象的组合关系(扩展对象对哪些目标对象进行功能扩展)
		
				<!-- 声明目标对象和扩展对象的组合关系(扩展对象对哪些目标对象进行功能扩展) -->
				<aop:config>
					<aop:aspect id="timeAspect" ref="timePrint">
						<!-- 切入点表达式的定义 -->
						<aop:pointcut expression="execution(public * *(..))" id="timePointcut"/>
						
						<!-- 定义前置通知，表示在符合切入点表达式的连接点之前执行功能扩展 -->
						<aop:before method="printStartTime" pointcut-ref="timePointcut"/>
						
						<!-- 定义后置通知，表示在符合切入点表达式的连接点之后执行功能扩展 -->
						<aop:after method="printEndTime" pointcut-ref="timePointcut"/>
					</aop:aspect>
				</aop:config>
			
		2-8) 测试
		
			ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
		
			//采用cglib代理(没有接口情况下)
			/*Object proxyObject = ac.getBean("targetDao"); 
			TargetDao proxyObj = (TargetDao)proxyObject; //因为目标对象没有接口，所以框架会采用cglib进行代理
			proxyObj.insertData(); */
			
			//采用JDK动态代理(有接口情况下)
			IDao proxyObject = (IDao)ac.getBean("targetDao");
			proxyObject.insertData();	
			
	4) 切入点表达式语法规则
	
			<!-- 切入点表达式的定义 -->
			<aop:pointcut expression="execution(public * *(..))" id="timePointcut"/>	
			
			
			public * *(..) 匹配所有的public修饰的方法，都进行功能扩展。
			
			public 表示方法的修饰符
			第一个*表示方法的返回类型任意
			第二个*表示方法的名称任意
			小括号表示方法
			..表示方法的参数任意
			
			
			具体语法规则参考：Spring2.5-中文参考手册.chm 6.2.3.4. 示例
			
	5) Spring框架默认采用JDK动态代理实现AOP功能扩展，如果目标对象没有接口，框架会采用Cglib进行代理。	
	
		Spring推荐面向接口编程。(一般情况下，服务层，数据访问层都要定义接口)	
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
				