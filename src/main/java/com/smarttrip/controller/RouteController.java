package com.smarttrip.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
import com.smarttrip.domain.Route;
import com.smarttrip.domain.RouteTheme;
import com.smarttrip.service.IRouteService;
import com.smarttrip.service.IRouteThemeService;
import com.smarttrip.service.IThemeService;

/**
 * @author : gaoweibupty@gamil.com
 * 创建时间 ： 2015年10月20日下午6:48:55
 */

@Controller
@RequestMapping("/route")
public class RouteController {
	
	@Autowired
	private IRouteService routeService;

	@Autowired
	private IRouteThemeService routeThemeService;
	
	@Autowired
	private IThemeService themeService;
	
	
	@RequestMapping("/top3")
	@ResponseBody
	public Result toTop3(Model model){
		Result result = new Result();
		List<Object> routeInfo3 = new ArrayList<>();
		List<Route> route3 = routeService.selectTop3ByDisplayOrder();
		for (int i =0; i < route3.size(); i++){
			// route, themes
			List<Object> routeInfo = new ArrayList<Object>();
			List<RouteTheme> routeThemes = routeThemeService.selectByRouteId(route3.get(i).getRouteId());
			List<String> themes = new ArrayList<String>();
			for (int j = 0; j < routeThemes.size(); j++){
				themes.add(themeService.selectByPrimaryKey(routeThemes.get(i).getThemeId()).getName());
			}
			routeInfo.add(route3.get(i));
			routeInfo.add(themes);
			routeInfo3.add(routeInfo);
		}
		result.setData(routeInfo3);
		return result;
	}
	
	@RequestMapping("/period")
	@ResponseBody
	public Result toPeriods(Model model){
		Result result = new Result();
		List<Integer> record = routeService.selectPeriods();
		HashSet<Integer> set = new HashSet<>();
		record.clear();
		record.addAll(set);
		HashMap<String, List<Integer>> data = new HashMap<>();
		data.put("天数", record);
		result.setData(data);
		return result;
	}
	
	@RequestMapping("/conditions")
	@ResponseBody
	public Result toRouteInConditions(HttpServletRequest request,Model model){
		Result result = new Result();
		String firstRegion = request.getParameter("firstRegion");
		String secondRegion =  request.getParameter("secondRegion");
		String theme = request.getParameter("theme");
		String periodString = request.getParameter("period");
//		String[] firstRegions = firstRegion.split(",");
//		String[] secondRegions = secondRegion.split(",");
//		String[] themes = theme.split(",");
//		String[] periods = period.split(",");
		if (firstRegion == null && secondRegion ==null && theme == null && periodString == null){
			result.setStatus("failed");
			return result;
		}
		int period = Integer.parseInt(periodString);
		result.setData(routeService.selectByConditions(firstRegion, secondRegion, theme, period));
		return result;
	}
	
	@RequestMapping("/details")
	@ResponseBody
	public Result toDetails(HttpServletRequest request,Model model){
		Result result = new Result();
		String routeId = request.getParameter("routeId");
		Route record = routeService.selectByPrimaryKey(routeId);
		if (record == null){
			result.setStatus("failed");
		}
		else{
			result.setData(record);
		}
		return result;
		
	}

}
