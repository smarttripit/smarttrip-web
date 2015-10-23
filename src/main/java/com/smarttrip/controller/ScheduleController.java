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
import com.smarttrip.domain.Schedule;
import com.smarttrip.service.IScheduleService;

/**
 * @author gaoweibupt@gmail.com
 * @version Date：2015年10月23日下午3:33:01
 */
@Controller
@RequestMapping("/schedule")
public class ScheduleController {

	@Autowired
	private IScheduleService scheduleService;
	
	@RequestMapping("/introduce")
	@ResponseBody
	public Result toIntroduce(HttpServletRequest request,Model model){
		Result result = new Result();
		String routeId = request.getParameter("routeId");
		List<Schedule> record = scheduleService.selectByRouteId(routeId);
		if (record == null){
			result.setStatus("failed");
		}else{
			result.setData(record);
		}
		return result;
	}
	
	@RequestMapping("/description")
	@ResponseBody
	public Result readDescription(HttpServletRequest request,Model model){
		String scheduleId = request.getParameter("scheduleId");
		Schedule schedule = scheduleService.selectByPrimaryKey(scheduleId);
		Result result = new Result();
		if (schedule == null){
			result.setStatus("failed");
			return result;
		}
		else{
			List<Object> record = new ArrayList<>();
			record.add(schedule.getDescription());
			record.add(schedule.getLink());
			result.setData(record);
			return result;
		}
		
	}
}
