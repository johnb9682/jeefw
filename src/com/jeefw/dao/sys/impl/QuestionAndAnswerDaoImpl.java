package com.jeefw.dao.sys.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import com.jeefw.dao.sys.DictDao;
import com.jeefw.dao.sys.QuestionAndAnswerDao;
import com.jeefw.model.sys.Dict;
import com.jeefw.model.sys.QuestionAndAnswer;

import core.dao.BaseDao;

/**
 * @author John
 */
@Repository
public class QuestionAndAnswerDaoImpl extends BaseDao<QuestionAndAnswer> implements QuestionAndAnswerDao {

	public QuestionAndAnswerDaoImpl() {
		super(QuestionAndAnswer.class);
	}
	
	@Override
	public List<Object[]> queryAnswerByQuestionIdSQL(Long questionId) {
		Query query = this.getSession().createSQLQuery(
				"SELECT * FROM jeefw.question_answer qa left join jeefw.answer a on qa.answerId = a.answerId where qa.questionId = ?;");
		query.setParameter(0, questionId);
		return query.list();
	}


}
