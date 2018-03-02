package com.jeefw.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jeefw.dao.sys.DictDao;
import com.jeefw.dao.sys.QuestionAndAnswerDao;
import com.jeefw.model.sys.Dict;
import com.jeefw.model.sys.QuestionAndAnswer;
import com.jeefw.service.sys.DictService;
import com.jeefw.service.sys.QuestionAndAnswerService;

import core.service.BaseService;

/**
 * @author John
 */
@Service
public class QuestionAndAnswerServiceImpl extends BaseService<QuestionAndAnswer> implements QuestionAndAnswerService {

	private QuestionAndAnswerDao questionAndAnswerDao;

	@Resource
	public void setQuestionAndAnswerDao(QuestionAndAnswerDao questionAndAnswerDao) {
		this.questionAndAnswerDao = questionAndAnswerDao;
		this.dao = questionAndAnswerDao;
	}

	@Override
	public QuestionAndAnswer queryByQuestionId(Long questionId) {
		return questionAndAnswerDao.getByProerties("questionId", questionId);  
	}

}
