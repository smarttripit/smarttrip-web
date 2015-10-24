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
import com.smarttrip.domain.Comment;
import com.smarttrip.service.ICommentService;

/**
 * @author gaoweibupt@gmail.com
 * @version Date：2015年10月23日下午7:50:18
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private ICommentService commentService;
	
	@RequestMapping("/route")
	@ResponseBody
	public Result readComments(HttpServletRequest request,Model model){
		String routeId = request.getParameter("routeId");
		Result result = new Result();
		List<Comment> record = commentService.selectByRouteId(routeId);
		if (record.size() == 0){
			result.setStatus("failed");
			return result;
		}else{
			List<Object> info = new ArrayList<>();
			info.add(routeId);
			info.add(record);
			result.setData(info);
			return result;
		}
	}
}
