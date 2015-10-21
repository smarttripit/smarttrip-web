package com.smarttrip.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
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
	public Result showUserJson(HttpServletRequest request,Model model){
		String userId = request.getParameter("id");
		User user = this.userService.selectByPrimaryKey(userId);
		Result rtn = new Result();
		rtn.setStatus("success");
		rtn.setData(user);
		return rtn;
	}
	
	@RequestMapping("/gotoLogin")
	public String gotoLogin(HttpServletRequest request,Model model){
		return "login";
	}
	
	@RequestMapping("/login")
	public String showLogin(HttpServletRequest request,Model model){
		String mobile = request.getParameter("mobile");
		String password = request.getParameter("password");
		User user = this.userService.selectByMobile(mobile);
		if (user == null)return "login";
		if (this.userService.checkPwd(user, password)){
			//session 中记录
			return "success";
		}
		return null;
	}
}
