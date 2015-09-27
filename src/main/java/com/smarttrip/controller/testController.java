/**
 * Date:2015年9月27日下午10:20:19
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Function: TODO ADD FUNCTION. <br/>
 * date: 2015年9月27日 下午10:20:19 <br/>
 *
 * @author songjiesdnu@163.com
 */
@Controller
@RequestMapping("/test")
public class testController {

	@RequestMapping("/velocity")
	public String velocity(Model model){
		String name = "velocity";
		model.addAttribute("name", name);
		return "test/velocity";
	}
}