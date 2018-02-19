package com.jeefw.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jeefw.dao.sys.QuestionDao;
import com.jeefw.model.sys.Question;
import com.jeefw.service.sys.QuestionService;

import core.service.BaseService; 

/**
 * @author John
 */
@Service
public class QuestionServiceImpl extends BaseService<Question> implements QuestionService {

	private QuestionDao questionDao;

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

}
