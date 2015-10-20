package com.smarttrip.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.domain.ClassicalRoute;
import com.smarttrip.service.IRouteService;

/**
 * @author : gaoweibupty@gamil.com
 * 创建时间 ： 2015年10月20日下午6:48:55
 */

@Controller
@RequestMapping("/route")
public class RouteController {
	
	@Autowired
	private IRouteService routeService;
	
	@RequestMapping("/top3")
	@ResponseBody
	public List<ClassicalRoute> toTop3(Model model){
		return this.routeService.selectTop3ByDisplayOrder();
	}
}
