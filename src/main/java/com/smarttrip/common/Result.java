package com.smarttrip.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * json返回数据类型. <br/>
 * date: 2015年10月26日 上午9:34:50 <br/>
 *
 * @author songjiesdnu@163.com
 */
public class Result {
	private static Logger logger = LoggerFactory.getLogger(Result.class);
	public static String SUCCESS = "success";
	public static String FAILED = "failed";
	
	private String status = SUCCESS;
	private String tipCode = "";
	private String tipMsg = "";
	private Object data;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		if(status == null  ||  (!status.equals(SUCCESS) &&  !status.equals(FAILED))){
			logger.error("status只允许以下值：" + SUCCESS + "、" + FAILED);
			throw new IllegalArgumentException("status只允许以下值：" + SUCCESS + "、" + FAILED);
		}
		this.status = status;
	}
	public String getTipCode() {
		return tipCode;
	}
	public void setTipCode(String tipCode) {
		this.tipCode = tipCode;
	}
	public String getTipMsg() {
		return tipMsg;
	}
	public void setTipMsg(String tipMsg) {
		this.tipMsg = tipMsg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
