package com.smarttrip.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
import com.smarttrip.service.IThemeService;

/**
 * @author : gaoweibupty@gamil.com
 * 创建时间 ： 2015年10月20日下午7:11:11
 */

@Controller
@RequestMapping("/theme")
public class ThemeController {
	
	@Autowired
	private IThemeService themeService;
	
	@RequestMapping("/all")
	@ResponseBody
	public Result readAllTheme(Model model){
		Result result = new Result();
		result.setData(this.themeService.selectAll());
		return result;
	}
}
