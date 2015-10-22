package com.smarttrip.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
import com.smarttrip.domain.Theme;
import com.smarttrip.domain.Visitor;
import com.smarttrip.domain.VisitorTheme;
import com.smarttrip.service.IThemeService;
import com.smarttrip.service.IVisitorService;
import com.smarttrip.service.IVisitorThemeService;
import com.smarttrip.util.MD5Utils;
import com.smarttrip.util.UUIDUtils;

@Controller
@RequestMapping("/visitor")
@ResponseBody
public class VisitorController {
	@Autowired
	private IVisitorService visitorService;
	private IVisitorThemeService visitorThemeService;
	private IThemeService themeService;
	
	/*
	 * 登录页
	 * 根据手机号/邮箱和密码登陆
	 */
	@RequestMapping("/login")
	public Result login(HttpServletRequest request,HttpSession session,Model model){
		Visitor visitor = new Visitor();
		Result result = new Result();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		if(userName == null||password == null){
			result.setStatus("fail");
			result.setTipMsg("用户名或密码为空");
		}else {
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
		if (visitor == null){ 
			result.setStatus("fail");
			result.setTipCode("login");
			result.setTipMsg("用户名不存在");
		}
		else if (visitor.getPassword().equals(password)){
			//session 中记录
			result.setStatus("success");
			session.setAttribute("visitorId", visitor.getVisitorId());
		}
		result.setStatus("fail");
		result.setTipCode("login");
		result.setTipMsg("密码错误");
		}
		return result;
		
	}
	
	/*
	 * 注册页
	 */
	
	//在注册页面，填写完用户名之后要检查该用户名是否已经被注册
	@RequestMapping("/isNameRegister")
	public Result isNameRegister(HttpServletRequest request,Model model){
		Result result = new Result();
		String name = request.getParameter("name");
		if(name == null){
			result.setStatus("fail");
			result.setTipMsg("用户名为空");
		} else {
		Visitor visitor = this.visitorService.selectByName(name);
		if(visitor == null){
			result.setStatus("true");
			result.setTipMsg("用户名未注册");
		}else{
			result.setStatus("false");
			result.setTipMsg("用户名已经被注册");
		}
		}
		return result;
	}
	
	//在注册页面，用户输入手机号之后要检查该手机号是否已经被注册。
	@RequestMapping("/isMobileNoRegister")
	public Result isMobileNoRegister(HttpServletRequest request,Model model){
		Result result = new Result();
		String mobileNo = request.getParameter("mobileNo");
		if(mobileNo == null) {
			result.setStatus("fail");
			result.setTipMsg("手机号为空");
		} else {
		Visitor visitor = this.visitorService.selectByMobileNo(mobileNo);
		if(visitor == null){
			result.setStatus("true");
			result.setTipMsg("手机号未注册");
		}else{
			result.setStatus("false");
			result.setTipMsg("手机号已经被注册");
		}
		}
		return result;
	}
	
	//提交注册时检查
	@RequestMapping("/register")
	public Result register(HttpServletRequest request,HttpSession session,Model model){
		Result result = new Result();
		String name = request.getParameter("name");
		String mobileNo = request.getParameter("mobileNo");
		String password = request.getParameter("password");
		String passwordAgain = request.getParameter("passwordAgain");
		String verifyCode = request.getParameter("verifyCode");
		if (name ==null ||mobileNo ==null ||password == null ||passwordAgain ==null || verifyCode == null){
			result.setStatus("fail");
			result.setTipMsg("出现空值");
			return result;
		}
	if(true){
//		if(this.nameCheck(name)
//				&&!this.nameReg(name)
//				&&this.mobileNoCheck(mobileNo)
//				&&!this.mobileNoReg(mobileNo)
//			    &&this.verifyCodeCheck(verifyCode)
//			    &&this.passwordCheck(password, passwordAgain)){
			Visitor visitor = new Visitor();
			String visitorId = UUIDUtils.getUUID();
			String salt = UUIDUtils.getUUID();
			String registerTime = DateFormatUtils.format(new Date(), "yyyy:mm:dd HH:mm:ss");
			visitor.setVisitorId(visitorId);
			visitor.setName(name);
			visitor.setMobileNo(mobileNo);
			visitor.setPassword(MD5Utils.encrypt(password + salt));
			visitor.setSalt(salt);
			visitor.setRegisterTime(registerTime);
			visitor.setEmailActivated("0");
			visitor.setStatus("1");
			visitor.setBirthdaySecret("1");
			if (this.visitorService.insert(visitor)!=0){
				result.setStatus("success");
				session.setAttribute("visitorId", this.visitorService.selectByMobileNo(mobileNo).getVisitorId());
				return result;
			} else {
				result.setStatus("fail");
				result.setTipMsg("注册不成功");
				return result;
			}	
		}
		else {
			result.setStatus("fail");
			result.setTipMsg("出现不合法注册字符或出现已注册内容");
			return result;
		}	
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
	@RequestMapping("/showVisitorInfo")
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
