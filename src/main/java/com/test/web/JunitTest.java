package com.test.web;

import com.test.bean.User;
import com.test.server.IUserServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class JunitTest {
	
	//此方法可行
	//@Resource
	//ApplicationContext context;
	
	@Resource
    IUserServer userService;

	@Test
	public void test() {
		/*ApplicationContext context = new ClassPathXmlApplicationContext(  
                "applicationContext.xml");*/  
        //IUserServer userService = (IUserServer) context.getBean("server");  
        User user = userService.testMethod("aa");
        System.out.println(user.getName());
	}


	@Test
	public void testTrans(){
		userService.testTrans(1,2,100);
	}

	@Test
	public void queryUser(){
		userService.queryUser("aa");
	}
	
	@Test
	public void clearCache(){
		
	}
	
	
	
	/*@Test
	public void test1() throws Exception{
		MemcachedClient client=new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		System.out.println(client.toString());
		
		Future fo=client.set("runoob", 900, "Free Education");
		
		System.out.println("set status:"+fo.get());
		
		CASValue casValue=client.gets("runoob");
		System.out.println("CASValue token:" +casValue);
		
	}*/

}
