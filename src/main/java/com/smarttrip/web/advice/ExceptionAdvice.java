/**
 * Date:2015年10月27日上午9:19:13
 * Copyright (c) 2015, songjiesdnu@163.com All Rights Reserved.
 */
package com.smarttrip.web.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

import com.smarttrip.common.Result;

/**
 * 异常处理. <br/>
 * date: 2015年10月27日 上午9:19:13 <br/>
 *
 * @author songjiesdnu@163.com
 */
@ControllerAdvice  
@ResponseBody  
public class ExceptionAdvice {
	private Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);
  
    /** 
     * 400 - Bad Request 
     */  
    @ResponseStatus(HttpStatus.BAD_REQUEST)  
    @ExceptionHandler(HttpMessageNotReadableException.class)  
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {  
        logger.error("参数解析失败[400]", e);  
        Result rtn = new Result();
        rtn.setStatus(Result.FAILED);
        rtn.setTipCode("400");
        rtn.setTipMsg("参数解析失败");
        return rtn;
    }  
  
    /** 
     * 405 - Method Not Allowed 
     */  
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)  
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)  
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {  
    	 logger.error("不支持当前请求方法", e);  
         Result rtn = new Result();
         rtn.setStatus(Result.FAILED);
         rtn.setTipCode("405");
         rtn.setTipMsg("不支持当前请求方法");
         return rtn;
    }  
  
    /** 
     * 415 - Unsupported Media Type 
     */  
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)  
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)  
    public Result handleHttpMediaTypeNotSupportedException(Exception e) {  
    	logger.error("不支持当前媒体类型[415]", e);  
        Result rtn = new Result();
        rtn.setStatus(Result.FAILED);
        rtn.setTipCode("415");
        rtn.setTipMsg("不支持当前媒体类型");
        return rtn;
    }  
  
    /** 
     * 500 - Internal Server Error 
     */  
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  
    @ExceptionHandler(Exception.class)  
    public Result handleException(Exception e) {  
        logger.error("服务运行异常[500]", e);  
        Result rtn = new Result();
        rtn.setStatus(Result.FAILED);
        rtn.setTipCode("500");
        String tipMsg = e.getMessage();
        if(tipMsg == null  ||  tipMsg.equals("")){
        	tipMsg = "服务运行异常";
        }
        rtn.setTipMsg(tipMsg);
        return rtn;
    }  
}  

