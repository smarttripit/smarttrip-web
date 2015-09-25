package com.smarttrip.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.domain.User;
import com.smarttrip.service.IUserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/showUser")
	public String showUser(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.selectByPrimaryKey(userId);
		model.addAttribute("user", user);
		return "showUser";
	}
	
	@RequestMapping("/showUserJson")
	@ResponseBody
	public User showUserJson(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.selectByPrimaryKey(userId);
		return user;
	}
}
