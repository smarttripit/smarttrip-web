package com.smarttrip.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smarttrip.domain.Theme;
import com.smarttrip.domain.Visitor;
import com.smarttrip.domain.VisitorTheme;
import com.smarttrip.service.IThemeService;
import com.smarttrip.service.IVisitorService;
import com.smarttrip.service.IVisitorThemeService;

@Controller
@RequestMapping("/visitor")

public class VisitorController {
	@Autowired
	private IVisitorService visitorService;
	private IVisitorThemeService visitorThemeService;
	private IThemeService themeService;
	
	/*
	 * 登录页
	 * 根据手机号/邮箱和密码登陆
	 */
	@RequestMapping("/visitor/login")
	public String login(HttpServletRequest request,Model model){
		Visitor visitor = new Visitor();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		Pattern patternPhone = Pattern.compile("^1[34589]\\d{9}$");
		Matcher matcherPhone = patternPhone.matcher(userName);
		Pattern patternEmail = Pattern.compile("^[0-9a-zA-Z_]+@[0-9a-zA-Z]+\\.[a-zA-Z]+$");
		Matcher matcherEmail = patternEmail.matcher(userName);
		if(matcherPhone.matches()){
			visitor = this.visitorService.selectByMobileNo(userName);	
		}else if(matcherEmail.matches()){
			visitor = this.visitorService.selectByEmail(userName);
		}else{
			visitor = this.visitorService.selectByName(userName);
		}
		if (visitor == null) 
			return "login";
		if (visitor.getPassword().equals(password)){
			//session 中记录
			return "success";
		}
		return null;	
	}
	
	/*
	 * 注册页
	 */
	
	//在注册页面，填写完用户名之后要检查该用户名是否已经被注册
	@RequestMapping("/visitor/isNameRegister")
	public boolean isNameRegister(HttpServletRequest request,Model model){
		String name = request.getParameter("name");
		Visitor visitor = this.visitorService.selectByName(name);
		return ((visitor == null||visitor.equals("")));
	}
	
	//在注册页面，用户输入手机号之后要检查该手机号是否已经被注册。
	@RequestMapping("/visitor/isMobileNoRegister")
	public boolean isMobileNoRegister(HttpServletRequest request,Model model){
		String mobileNo = request.getParameter("mobileNo");
		Visitor visitor = this.visitorService.selectByMobileNo(mobileNo);
		return ((visitor == null||visitor.equals("")));
	}
	
	//提交注册时检查
	@RequestMapping("/visitor/register")
	public int register(HttpServletRequest request,Model model){
		String name = request.getParameter("name");
		String mobileNo = request.getParameter("mobileNo");
		String password = request.getParameter("password");
		String passwordAgain = request.getParameter("passwordAgain");
		String verifyCode = request.getParameter("verifyCode");
		if(this.nameCheck(name)
				&&!this.nameReg(name)
				&&this.mobileNoCheck(mobileNo)
				&&!this.mobileNoReg(mobileNo)
			    &&this.verifyCodeCheck(verifyCode)
			    &&this.passwordCheck(password, passwordAgain)){
			Visitor visitor = new Visitor();
			visitor.setName(name);
			visitor.setMobileNo(mobileNo);
			visitor.setPassword(passwordAgain);
			return (this.visitorService.insert(visitor));
		}
		else 
			return 0;	
	}
	//注册页依赖函数
	public boolean nameCheck(String name) {
		Pattern patternName = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]{4,15}$");
		Matcher matcherName = patternName.matcher(name);
		return matcherName.matches();
	}
	
	public boolean mobileNoCheck(String mobileNo) {
		Pattern patternMobileNo = Pattern.compile("^1[34589]\\d{9}$");
		Matcher matcherMobileNo = patternMobileNo.matcher(mobileNo);
		return matcherMobileNo.matches();
	}
	
	public boolean verifyCodeCheck(String verifyCode) {
		return true;
	}
	
	public boolean passwordCheck(String password , String passwordAgain ) {
		return (password.length() >= 6 && password.equals(passwordAgain));
	}
	
	public boolean nameReg(String name) {
		Visitor visitor = this.visitorService.selectByName(name);
		return ((visitor == null||visitor.equals("")));
	}
	
	public boolean mobileNoReg(String mobileNo) {
		Visitor visitor = this.visitorService.selectByName(mobileNo);
		return ((visitor == null||visitor.equals("")));
	}
	
	
	/*
	 * 游客中心－游客个人信息
	 */
	
	//读取游客信息
	@RequestMapping("/visitor/showVisitorInfo")
	public List<Object> showVisitorInfo(HttpServletRequest request,Model model){
		HttpSession session = request.getSession();
		String visitorId = (String)session.getAttribute("visitorId");
		Visitor visitor = this.visitorService.selectByPrimaryKey(visitorId);
		List<VisitorTheme> visitorThemes = this.visitorThemeService.selectByVisitorId(visitorId);
		List<Theme> themes = new ArrayList<Theme>();
		for(VisitorTheme visitorTheme : visitorThemes){
		String themeId = visitorTheme.getThemeId();
		Theme theme = this.themeService.selectByPrimaryKey(themeId);
		themes.add(theme);
		}
		List<Object> visitorInfo = new ArrayList<Object>();
		visitorInfo.add(visitor.getCity());
		visitorInfo.add(themes);
		return visitorInfo;
		
		//return list	此处一个visitorId可能对应多个theme，需要修改
	}
	
//	@RequestMapping("/visitor/modifyVisitorInfo")
//	public boolean modifyVisitorInfo(HttpServletRequest request,Model model){
//		String realName = request.getParameter("realName");
//		String realNameSecret = request.getParameter("realNameSecret");
//		String gender = request.getParameter("gender");
//		
//	}

}
