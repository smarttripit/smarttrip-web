package com.smarttrip.web.controller.base;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smarttrip.common.Result;
import com.smarttrip.common.SessionUtil;
import com.smarttrip.domain.Visitor;
import com.smarttrip.service.IPhoneAuthCodeService;
import com.smarttrip.service.IVisitorService;

/**
 * 文件上传和下载
 * @author songjie
 *
 */
@Controller
@RequestMapping("/authCode")
public class AuthCodeController {
	private Logger logger = LoggerFactory.getLogger(AuthCodeController.class);
	
	@Autowired
	IPhoneAuthCodeService phoneAuthCodeService;
	
	@Autowired
	IVisitorService visitorService;
	
	/**
	 * 发送短信验证码
	 * @author songjiesdnu@163.com
	 * @param mobileNo
	 * @param session
	 * @return
	 */
	@RequestMapping("/send")
	@ResponseBody
	public Result send(@RequestParam(value="mobileNo",required=false) String mobileNo,
					   HttpSession session){
		logger.debug("进入send方法");
		Result rtn = new Result();
		String visitorId = SessionUtil.getVisitorId(session);
		if(visitorId != null){
			mobileNo = visitorService.selectByPrimaryKey(visitorId).getMobileNo();
		}else{
			if(mobileNo == null  ||  mobileNo.equals("")){
				rtn.setStatus("failed");
				rtn.setTipCode("emptyMobileNo");
				rtn.setTipMsg("手机号不能为空");
				return rtn;
			}
			if(!mobileNo.matches("^\\d{11}$")){
				rtn.setStatus("failed");
				rtn.setTipCode("wrongMobileType");
				rtn.setTipMsg("手机号格式不对");
				return rtn;
			}
		}
		Map<String, String> result = phoneAuthCodeService.send(mobileNo);
		rtn.setStatus(result.get("result"));
		rtn.setTipCode(result.get("tipCode"));
		rtn.setTipMsg(result.get("tipMsg"));
		logger.debug("退出send方法");
		return rtn;
	}
	
	/**
	 * 校验短信验证码
	 * @author songjiesdnu@163.com
	 * @param mobileNo
	 * @param userCode
	 * @param session
	 * @return
	 */
	@RequestMapping("/verify")
	@ResponseBody
	public Result verify(@RequestParam(value="mobileNo",required=false) String mobileNo,
						 @RequestParam(value="userCode",required=true) String userCode,
						 HttpSession session){
		logger.debug("进入verify方法");
		Result rtn = new Result();
		if(userCode == null  ||  userCode.equals("")){
			rtn.setStatus("failed");
			rtn.setTipCode("emptyUserCode");
			rtn.setTipMsg("验证码不能为空");
			return rtn;
		}
		String visitorId = SessionUtil.getVisitorId(session);
		if(visitorId != null){
			Visitor vistor = visitorService.selectByPrimaryKey(visitorId);
			mobileNo = vistor.getMobileNo();
		}else{
			if(mobileNo == null  ||  mobileNo.equals("")){
				rtn.setStatus("failed");
				rtn.setTipCode("emptyMobileNo");
				rtn.setTipMsg("手机号不能为空");
				return rtn;
			}
		}
		Map<String, String> result = phoneAuthCodeService.verify(mobileNo, userCode);
		if(result.get("result").equals("right")){
			rtn.setStatus("success");
		}else{
			rtn.setStatus("failed");
		}
		rtn.setTipCode(result.get("result"));
		rtn.setTipMsg(result.get("tipMsg"));
		logger.debug("退出verify方法");
		return rtn;
	}
}
