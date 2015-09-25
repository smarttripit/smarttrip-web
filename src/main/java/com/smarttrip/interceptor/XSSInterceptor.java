/**
 * Date:2015年7月22日下午3:59:48
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.interceptor;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 拦截xss攻击。目前只针对http请求做xss攻击拦截。<br/>
 * date: 2015年7月22日 下午3:59:48 <br/>
 *
 * @author songjiesdnu@163.com
 * @version 
 * @since JDK 1.6
 */
public class XSSInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory.getLogger(XSSInterceptor.class);
	
	private static Pattern[] patterns = new Pattern[]{
        // Script fragments
        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
        // src='...'
        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // lonely script tags
        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // eval(...)
        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // expression(...)
        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
        // javascript:...
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        // vbscript:...
        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
        // onload(...)=...
        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };
	
	/**
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
		throws Exception {
		logger.debug("xss攻击防御-处理http请求-开始");
		Map<String, String[]> parameterMap = request.getParameterMap();
		Iterator<String> keyIt = parameterMap.keySet().iterator();
		while(keyIt.hasNext()){
			String key = keyIt.next();
			String[] values = request.getParameterValues(key);
			for(String value : values){
				logger.debug("{}={}", key, value);
				if(!isValid(value)){
					logger.error("疑似xss攻击，已被拦截。URL:{}；参数:{}={}", request.getRequestURL(), key, value);
            		throw new IllegalArgumentException("疑似xss攻击，已被拦截");
				}
			}
		}
		logger.debug("xss攻击防御-处理http请求-结束");
		return true;
	}
	
	/**
	 * 判断参数是否合法。
	 * @author songjiesdnu@163.com
	 * @param value
	 * @return true：合法（不存在xss攻击脚本）；false：不合法（存在xss攻击脚本）
	 * @since JDK 1.6
	 */
	private static boolean isValid(String value){
		if(value != null){
			if (value != null) {
	            value = value.replaceAll("\\s", "");
	            for (Pattern scriptPattern : patterns){
	            	Matcher matcher = scriptPattern.matcher(value);
	            	if(matcher.matches()){
	            		return false;
	            	}
	            }
	        }
		}
		return true;
	}
}