package com.smarttrip.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
import com.smarttrip.domain.Region;
import com.smarttrip.service.IRegionService;

/**
 * @author : gaoweibupty@gamil.com
 * 创建时间 ： 2015年10月20日下午7:21:21
 */

@Controller
@RequestMapping("/region")
public class RegionController {

	@Autowired
	private IRegionService regionService;
	
	@RequestMapping("/city")
	@ResponseBody
	public Result readByCity(HttpServletRequest request,Model model){
		Result result = new Result();
		String city = request.getParameter("city");
		List<Region> record =  regionService.selectByFirstRegion(city);
		result.setData(record);
		return result;
	}
}
