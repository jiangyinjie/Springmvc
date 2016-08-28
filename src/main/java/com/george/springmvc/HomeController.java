package com.george.springmvc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	// 默认用户名
	private	static final String USERNAME = "123";
	// 默认密码
	private	static final String PWD = "123";
	
	
	static final String FILE_DIR = "D:\\";
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request) throws UnsupportedEncodingException {
		String username = request.getParameter("username");
		String pwd = request.getParameter("pwd");
		
		if(username != null) {
			byte[] bytes = username.getBytes("ISO8859-1");
			username = new String(bytes, "UTF-8");
		}
		
		if(pwd != null) {
			byte[] bytes = pwd.getBytes("ISO8859-1");
			pwd = new String(bytes, "UTF-8");
		}
		System.out.println("用户名="+username+", 密码="+pwd);
		
		String message = "";
		boolean success = false;
	
		if (!USERNAME.equals(username)) { 
			success = false;
			message = "login name or password error";
		} else if (!PWD.equals(pwd)) { 
			success = false;
			message = "login name or password error";
		} else { //登陆成功
			success = true;
			message = "login success";
		}

		Map<String, Object> map = new HashMap<String, Object>();
		
		String key = success ? "success" : "error";
		
		map.put(key, message);
		return map;
	}
	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> UploadAction(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws IllegalStateException, IOException {
		
		String username = request.getParameter("username");
		String message = "";
		boolean success = false;
		if (file != null) {
			String fileName = file.getOriginalFilename(); 
			success = true;
			message = "upload success";
			File targetFile = new File(FILE_DIR, fileName); 
			file.transferTo(targetFile);
			System.out.println(username + "用户名" + fileName + "文件名");
		} else {
			success = false;
			message = "upload failed";
		}

		Map<String, Object> map = new HashMap<String, Object>();
		
		String key = success ? "success" : "error";
		
		map.put(key, message);
		return map;
	}
}
