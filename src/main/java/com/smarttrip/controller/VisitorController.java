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
	@ResponseBody
	public Result login(HttpServletRequest request,HttpSession session,Model model){
		Visitor visitor = new Visitor();
		Result result = new Result();
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		if(userName == null ||userName.equals("")||password == null || password.equals("")){
			result.setStatus("fail");
			result.setTipMsg("用户名或密码为空");
			return result;
		}
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
			return result;
		}
		
		if (visitor.getPassword().equals(MD5Utils.encrypt(password+visitor.getSalt()))){
			//session 中记录
			result.setStatus("success");
			session.setAttribute("visitorId", visitor.getVisitorId());
			return result;
		}
		
		result.setStatus("fail");
		result.setTipCode("login");
		result.setTipMsg("密码错误");
		return result;	
	}
	
	/*
	 * 注册页
	 */
	
	//在注册页面，填写完用户名之后要检查该用户名是否已经被注册
	@RequestMapping("/isNameRegister")
	@ResponseBody
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
	@ResponseBody
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
	@ResponseBody
	public Result register(HttpServletRequest request , HttpSession session,Model model){
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
	
		if(this.nameCheck(name)
				&&this.nameReg(name)
				&&this.mobileNoCheck(mobileNo)
				&&this.mobileNoReg(mobileNo)
			    &&this.verifyCodeCheck(verifyCode)
			    &&this.passwordCheck(password, passwordAgain)){
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
			visitor.setBirthdaySecret("0");
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
		Visitor visitor = this.visitorService.selectByMobileNo(mobileNo);
		return ((visitor == null||visitor.equals("")));
	}
	
	
	/*
	 * 游客中心－游客个人信息
	 */
	
	//读取游客信息
	@RequestMapping("/showVisitorInfo")
	@ResponseBody
	public Result showVisitorInfo(HttpServletRequest request,HttpSession session,Model model){ 
		Result result = new Result();
		Object id = session.getAttribute("visitorId");
		if( id == null || id.toString().equals("")){
			result.setStatus("fail");
			result.setTipMsg("游客未登陆");
			return result;
		}
		String visitorId = (String)id;
		Visitor visitor = this.visitorService.selectByPrimaryKey(visitorId);
		if( visitor == null){
			result.setStatus("fail");
			result.setTipMsg("不存在该visitorId的用户");
			return result;
		}
		List<VisitorTheme> visitorThemes = this.visitorThemeService.selectByVisitorId(visitorId);
		List<Theme> themes = new ArrayList<Theme>();
		if(visitorThemes != null){
		for(VisitorTheme visitorTheme : visitorThemes){
		String themeId = visitorTheme.getThemeId();
		Theme theme = this.themeService.selectByPrimaryKey(themeId);
		themes.add(theme);
		}
		}
		else{
			themes = null;
		}
		List<Object> visitorInfo = new ArrayList<Object>();
		visitorInfo.add(visitor.getRealName());
		visitorInfo.add(visitor.getRealNameSecret());
		visitorInfo.add(visitor.getGender());
		visitorInfo.add(visitor.getBirthday());
		visitorInfo.add(visitor.getBirthdaySecret());
		visitorInfo.add(visitor.getCity());
		visitorInfo.add(visitor.getProfession());
		visitorInfo.add(visitor.getEducation());
		visitorInfo.add(visitor.getIntroduction());
		visitorInfo.add(visitor.getWebsite());
		visitorInfo.add(themes);
		result.setStatus("success");
		result.setData(visitorInfo);
		return result;
	}
	
	@RequestMapping("/modifyVisitorInfo")
	@ResponseBody
	public Result modifyVisitorInfo(HttpServletRequest request, HttpSession session , Model model){
		Result result = new Result();
		Visitor visitor = new Visitor();
		Object id = session.getAttribute("visitorId");
		if( id == null || id.toString().equals("")){
			result.setStatus("fail");
			result.setTipMsg("游客未登陆");
			return result;
		}
		String visitorId = (String)id;
		String realName = request.getParameter("realName");
		String realNameSecret = request.getParameter("realNameSecret");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("birthday");
		String birthdaySecret = request.getParameter("birthdaySecret");
		String city = request.getParameter("city");
		String profession = request.getParameter("profession");
		String education = request.getParameter("education");
		String introduction = request.getParameter("introduction");
		String website = request.getParameter("website");
		String[] themeNames =request.getParameterValues("themeNames");
		visitor.setVisitorId(visitorId);
		visitor.setRealName(realName);
		visitor.setRealNameSecret(realNameSecret);
		visitor.setGender(gender);
		visitor.setBirthday(birthday);
		visitor.setBirthdaySecret(birthdaySecret);
		visitor.setCity(city);
		visitor.setProfession(profession);
		visitor.setEducation(education);
		visitor.setIntroduction(introduction);
		visitor.setWebsite(website);
		if(themeNames!= null){
			for(String name : themeNames){
			Theme theme = this.themeService.selectByName(name);
			this.visitorThemeService.deleteByVisitorId(visitorId);
				VisitorTheme visitorTheme = new VisitorTheme();
				visitorTheme.setId(UUIDUtils.getUUID());
				visitorTheme.setVisitorId(visitorId);
				visitorTheme.setThemeId(theme.getThemeId());
				this.visitorThemeService.insert(visitorTheme);
			}
		}
		this.visitorService.updateByPrimaryKey(visitor);
		result.setStatus("success");
		return result;
	}

}
