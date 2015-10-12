package com.smarttrip.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.smarttrip.platform.file.AliyunFileManager;

/**
 * 文件上传和下载
 * @author songjie
 *
 */
@Controller
@RequestMapping("/file")
public class FileController {
	private Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Autowired
	AliyunFileManager aliyunFileManager;
	
	/**
	 * 跳转到文件上传页面
	 * @return
	 */
	@RequestMapping("/gotoUploadPage")
	public String gotoUploadPage(){
		return "file/uploadPage";
	}
	
	/**
	 * 接收上传的非可执行文件
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/upload/notExe")
	@ResponseBody
	public Map<String, String> uploadNotExe(HttpServletRequest request,Model model,
			@RequestParam(value="fileUpload") MultipartFile fileUpload,
			@RequestParam(value="title") String title){
		logger.debug("uploadNotExe--开始");
		Map<String, String> rtn = new HashMap<String, String>();
		rtn.put("status", "success");
		rtn.put("tips", "文件上传成功");
		
		if(title == null  ||  title.equals("")){
			rtn.put("status", "failed");
			rtn.put("tips", "title不能为空");
			return rtn;
		}
		
		String fileValidateResult = this.validateUploadFile(fileUpload);
		if(!fileValidateResult.equals("ok")){
			rtn.put("status", "failed");
			rtn.put("tips", fileValidateResult);
			return rtn;
		}
		
		String saveResultString = saveFile(fileUpload);
		if(!saveResultString.equals("ok")){
			rtn.put("status", "failed");
			rtn.put("tips", saveResultString);
			return rtn;
		}
		logger.debug("uploadNotExe--结束");
		return rtn;
	}
	
	/**
	 * 校验用户上传的文件
	 * @param fileUpload
	 * @return ok:符合要求  其他：返回提示信息
	 */
	private String validateUploadFile(MultipartFile fileUpload){
		if(fileUpload.isEmpty()){
			logger.info("上传的文件内容为空");;
			return "上传的文件为空";
		}
		return "ok";
	}
	
	/**
	 * 保存用户上传的文件
	 * @param fileUpload
	 * @return
	 */
	private String saveFile(MultipartFile fileUpload){
		logger.debug("saveFile-开始");
		String fileName = fileUpload.getOriginalFilename();
		fileUpload.getContentType();
		try {
			aliyunFileManager.upload(fileName, fileUpload.getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		logger.debug("saveFile-结束");
		return "ok";
	}
}
