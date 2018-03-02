package com.jeefw.dao.sys.impl;

import java.util.List;
import java.util.Objects;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.jeefw.dao.sys.AnswerDao;
import com.jeefw.dao.sys.DictDao;
import com.jeefw.dao.sys.QuestionAndAnswerDao;
import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Dict;
import com.jeefw.model.sys.QuestionAndAnswer;

import core.dao.BaseDao;

/**
 * @author John
 */
@Repository
public class AnswerDaoImpl extends BaseDao<Answer> implements AnswerDao {

	public AnswerDaoImpl() {
		super(Answer.class);
	}

	@Override
	public List<Object[]> getAnswerWithMaxId() {
		Query query = this.getSession().createSQLQuery("SELECT * FROM answer WHERE answerId = (SELECT MAX(answerId) FROM answer)");
		return query.list();
	}

}
