/**
 * Date:2015年10月23日下午3:36:41
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.common;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * session工具类. <br/>
 * date: 2015年10月23日 下午3:36:41 <br/>
 *
 * @author songjiesdnu@163.com
 */
public class SessionUtil {
	private static Logger logger = LoggerFactory.getLogger(SessionUtil.class);
	/**
	 * 判断游客是否登录
	 * @author songjiesdnu@163.com
	 * @param session
	 * @return
	 */
	public static boolean hasLogin(HttpSession session){
		logger.debug("进入hasLogin方法");
		String visitorId = SessionUtil.getVisitorId(session);
		if(visitorId == null){
			return false;
		}
		logger.debug("退出hasLogin方法");
		return true;
	}
	
	/**
	 * 获取游客的id。其实该方法完全可以替代hasLogin方法。
	 * @author songjiesdnu@163.com
	 * @param session
	 * @return  null：获取不到游客的id，说明游客未登录；否则，返回游客的id。
	 */
	public static String getVisitorId(HttpSession session){
		logger.debug("进入getVisitorId方法");
		Object o = session.getAttribute("visitorId");
		String visitorId = null;
		if(o != null){
			return o.toString();
		}
		logger.debug("退出getVisitorId方法");
		return visitorId;
	}
	
	/**
	 * 退出登录
	 * @param session
	 */
	public static void logOut(HttpSession session){
		logger.debug("进入logOut方法");
		session.removeAttribute("visitorId");
		logger.debug("退出logOut方法");
	}
}