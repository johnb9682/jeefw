package com.jeefw.dao.sys;

import java.util.List;

import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Dict;
import com.jeefw.model.sys.QuestionAndAnswer;

import core.dao.Dao;

/**
 * @author John
 */
public interface AnswerDao extends Dao<Answer> {

	List<Object[]> getAnswerWithMaxId();

}
