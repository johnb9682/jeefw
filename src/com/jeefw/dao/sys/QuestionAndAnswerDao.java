package com.jeefw.dao.sys;

import java.util.List; 

import com.jeefw.model.sys.QuestionAndAnswer;

import core.dao.Dao;

/**
 * @author John
 */
public interface QuestionAndAnswerDao extends Dao<QuestionAndAnswer> {
	public List<Object[]> queryAnswerByQuestionIdSQL(Long questionId);
}
