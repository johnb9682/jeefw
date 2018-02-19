package com.jeefw.controller.sys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeefw.core.Constant;
import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.QuestionService; 


/**
 * @author John
 * @Slack Redirect Response
 */
@Controller
@RequestMapping("/question")
public class SlackResponseController extends JavaEEFrameworkBaseController<SysUser> implements Constant { 

	@Resource
	private QuestionService questionService;
	
	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();  
		result.put("result", 200);
		writeJSON(response, result);
	}
	
	@RequestMapping(value = "/ask", method = { RequestMethod.POST})
	public void ask(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		Map<String, Object> result = new HashMap<String, Object>();
		// result.put("result", 200);
		String inputText = request.getParameter("text");
		result.put("text", questionService.queryByQuestionContent(inputText));
		result.put("inputtext", inputText);
		// result.put("username", request.getParameter("user_name"));
		// result.put("mrkdwn", true);
		writeJSON(response, result);
	}

	
}
