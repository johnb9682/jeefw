package com.jeefw.service.sys;
 
import java.util.Date;

import net.sf.json.JSONArray;

import com.jeefw.model.sys.Answer; 

import core.service.Service;

public interface AnswerService extends Service<Answer> {
	public Answer getAnswer(Long answerId);
	String generateAuthorAndDate(String author, Date date);
	JSONArray generateFieldWithAuthorAndDate(String authorString, Date date);
	Long getMaxAnswerId();
}
