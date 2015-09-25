/**
 * 
 */
package com.smarttrip.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.smarttrip.domain.User;
import com.smarttrip.service.IUserService;

/**
 * @author songjie
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})
public class TestMyBatis {
	private static Logger logger = LoggerFactory.getLogger(TestMyBatis.class);
//	private ApplicationContext ac = null;
	@Resource
	private IUserService userService = null;

//	@Before
//	public void before() {
//		ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//		userService = (IUserService) ac.getBean("userService");
//	}

	@Test
	public void test1() { 
		User user = userService.selectByPrimaryKey("1");
		logger.info("中文");
		System.err.println("汉子");
		// System.out.println(user.getUserName());
		// logger.info("值："+user.getUserName());
		logger.info("user:{}", JSON.toJSONString(user));
		JSON json = (JSON) JSON.toJSON(user);
		User user2 = JSON.toJavaObject(json, user.getClass());
		logger.info("user2:{}", JSON.toJSONString(user2));
	}
}

