package com.smarttrip.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.smarttrip.domain.Region;
import com.smarttrip.domain.Route;
import com.smarttrip.domain.RouteTheme;
import com.smarttrip.service.IRegionService;
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
	
	@Autowired
	private IRegionService regionService;
	
	
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
	
	@RequestMapping("/condition")
	@ResponseBody
	public Result toRouteInConditions(HttpServletRequest request,Model model){
		Result result = new Result();
		String firstRegion = request.getParameter("firstRegion");
		String secondRegion =  request.getParameter("secondRegion");
		String theme = request.getParameter("theme");
		String periodString = request.getParameter("period");
		if (firstRegion == null && secondRegion ==null && theme == null && periodString == null){
			result.setStatus("failed");
			return result;
		}
		String[] secondRegions = secondRegion.split(",");
		String[] themes = theme.split(",");
		String[] periodStrings = periodString.split(",");
		int[] periods = new int[periodStrings.length];
		for (int i = 0; i < periods.length; i++){
			periods[i] = Integer.parseInt(periodStrings[i]);
		}
		
		List<Route> record = new ArrayList<>();
		for (int i = 0 ; i < secondRegions.length || i == 0; i++){
			for (int j = 0; j < themes.length || j == 0; j++){
				for (int k = 0; k < periods.length || k == 0; k++){
					record.addAll(routeService.selectByConditions(firstRegion, secondRegions[i], themes[i], periods[i]));
				}	
			}
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
	
	@RequestMapping("/conditions")
	@ResponseBody
	public Result readRouteByConditions(HttpServletRequest request,Model model){
		Result result =  new Result();
		//大地区只能有一个
		String firstRegion = request.getParameter("firstRegion");
		String secondRegionString = request.getParameter("secondRegion");
		String themeString = request.getParameter("theme");
		String periodString = request.getParameter("period");
		String pageNumString = request.getParameter("pageNum");
		int pageNum = Integer.parseInt(pageNumString);
		String pageSizeString = request.getParameter("pageSize");
		int pageSize = Integer.parseInt(pageSizeString);
		String sortField = request.getParameter("sortField");
		//String[] firstRegion = firstRegionString.split(",");
		List<String> secondRegion = Arrays.asList(secondRegionString.split(","));
		List<String> theme = Arrays.asList(themeString.split(","));
		String[] periodStrings = periodString.split(",");
		int[] period = new int[periodStrings.length];
		for (int i = 0; i < periodStrings.length; i++){
			period[i] = new Integer(Integer.parseInt(periodStrings[i]));
		}
		List<String> regionId = regionService.selectRegionId(firstRegion, secondRegion);
		
		List<Route> record = routeService.selectByConditions(regionId, theme, period, pageNum, pageSize, sortField);
		result.setData(record);
		return result;
	}

}
