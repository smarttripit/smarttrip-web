/**
 * Date:2015年8月20日上午10:30:33
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import mockit.Mock;
import mockit.MockUp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.smarttrip.domain.User;
import com.smarttrip.service.impl.UserService;

/**
 * UserController的单测. <br/>
 * date: 2015年8月20日 上午10:30:33 <br/>
 *
 * @author songjiesdnu@163.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring-mvc.xml",
								   "classpath:spring-mybatis.xml"})
public class UserControllerTest {
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void test1(){
		assertTrue(true);
	}
	
	@Test
	public void getAccount() throws Exception {
		new MockUp<UserService>(){
			@Mock
			User selectByPrimaryKey(String id){
				User user = new User();
				user.setEmail("12312@qq.com");
				user.setId("id1");
				user.setMobileNo("15201111111");
				user.setName("name");
				user.setNickName("nickName");
				user.setPassword("password");
				user.setRealName("realname");
				user.setRegisterTime("registerTime");
				user.setSalt("salt");
				user.setStatus("1");
				return user;
			}
		};
		
		this.mockMvc.perform(get("/user/showUserJson?id=1")
				.accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$.id").value("id1"));
	}
}
