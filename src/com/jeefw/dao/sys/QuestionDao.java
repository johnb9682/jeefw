package com.jeefw.dao.sys;

import java.util.List;
 
import com.jeefw.model.sys.Question;

import core.dao.Dao;

/**
 * @author John
 */
public interface QuestionDao extends Dao<Question> {

	void indexingQuestion();

	List<Question> queryByQuestionContent(String question, int maxResults);

}
