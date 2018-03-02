package com.jeefw.service.sys.impl;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service; 

import com.jeefw.dao.sys.AnswerDao;
import com.jeefw.model.sys.Answer;
import com.jeefw.service.sys.AnswerService;

import core.service.BaseService;
import core.support.DateTimeSerializer;

/**
 * @author John
 */
@Service
public class AnswerServiceImpl extends BaseService<Answer> implements AnswerService {

	private AnswerDao answerDao;

	@Resource
	public void setAnswerDao(AnswerDao answerDao) {
		this.answerDao = answerDao;
		this.dao = answerDao;
	}

	@Override
	public Answer getAnswer(Long answerId) {
		return answerDao.getByProerties("answerId", answerId);
		// return answerDao.get(answerId);
	}

	@Override
	public String generateAuthorAndDate(String authorString, Date date) {
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM); 
		return "Author: " + authorString + "\n" + "Last Modified: " + dateFormat.format(date);  
	}
	@Override
	public JSONArray generateFieldWithAuthorAndDate(String authorString, Date date) { 
		
		JSONArray fields = new JSONArray(); 
		JSONObject author = new JSONObject(); 
		// author.put("title", "Author");
		author.put("title", "Author: " + authorString);
		author.put("short", true);
		fields.add(author);
		JSONObject lastModified = new JSONObject(); 
		// lastModified.put("title", "Last Modified");
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
		lastModified.put("title", "Last Modified: " + dateFormat.format(date));
		lastModified.put("short", true);
		fields.add(lastModified);
		return fields;
	}

	// This code is so ugly !!!
	// Change it later
	@Override
	public Long getMaxAnswerId() {
		List<Object[]> answerList = answerDao.getAnswerWithMaxId();
		JSONObject answer = new JSONObject();    
		for(ListIterator<Object[]> iter = answerList.listIterator(); iter.hasNext();){ 
			
			Object[] element = iter.next();    
			
			answer.put("answerId", element[0]);  
			answer.put("answer", element[1]);     
		}   
		return Long.valueOf(answer.get("answerId") + ""); 
	}
}
