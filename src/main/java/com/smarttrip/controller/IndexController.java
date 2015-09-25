package com.smarttrip.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 首页
 * @author songjie
 *
 */
@Controller
@RequestMapping("/index")
public class IndexController {
	
	@RequestMapping
	public String toIndex(HttpServletRequest request,Model model){
		String contextPath = request.getContextPath();
		model.addAttribute("contextPath", contextPath);
		return "index";
	}
}
