package com.jeefw.service.sys.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.jeefw.dao.sys.AnswerDao;
import com.jeefw.dao.sys.QuestionAndAnswerDao;
import com.jeefw.dao.sys.QuestionDao;
import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Question;
import com.jeefw.model.sys.QuestionAndAnswer;
import com.jeefw.service.sys.QuestionService;

import core.service.BaseService; 

/**
 * @author John
 */
@Service
public class QuestionServiceImpl extends BaseService<Question> implements QuestionService {

	private QuestionDao questionDao;
	private QuestionAndAnswerDao questionAndAnswerDao;
	private AnswerDao answerDao;

	private int MAX_RESULT = 10;
	
	@Resource
	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
		this.dao = questionDao;
	}

	@Override
	public void indexingQuestion() {
		questionDao.indexingQuestion();
		
	}

	@Override
	public List<Question> queryByQuestionContent(String question) {
		return questionDao.queryByQuestionContent(question, MAX_RESULT);
	}

	@Override
	public Answer queryAnswerByQuestionId(Long questionId) {
		System.out.println();
		List<Object[]> result = questionAndAnswerDao.queryAnswerByQuestionIdSQL(questionId);
		System.out.println(result);
		QuestionAndAnswer questionAndAnwser = null;
		try{
			questionAndAnwser = questionAndAnswerDao.getByProerties("questionId", questionId);
		}
		catch(NullPointerException nullPointerException){
			
		}
		
		if(Objects.nonNull(questionAndAnwser)) {
			return answerDao.get(questionAndAnwser.getAnswerId());  
		} else {
			return answerDao.get(questionId);
		}
		// return null;
	}

	@Override
	public Long getMaxQuestionId() {
		List<Object[]> questionList = questionDao.getQuestionWithMaxId();
		JSONObject question = new JSONObject();    
		for(ListIterator<Object[]> iter = questionList.listIterator(); iter.hasNext();){ 
			
			Object[] element = iter.next();    
			
			question.put("questionId", element[0]);  
			question.put("question", element[1]);     
		}   
		return Long.valueOf(question.get("questionId") + ""); 
	}

}
