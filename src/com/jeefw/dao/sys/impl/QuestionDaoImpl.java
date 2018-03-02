package com.jeefw.dao.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;

import com.jeefw.dao.sys.QuestionDao; 
import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Question;

import core.dao.BaseDao; 

/**
 * @author John
 */
@Repository
public class QuestionDaoImpl extends BaseDao<Question> implements QuestionDao {

	public QuestionDaoImpl() {
		super(Question.class);
	} 

	@Override
	public void indexingQuestion() {
		try {
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			// Object question = fullTextSession.load(Question.class, new BigDecimal(99));
			// fullTextSession.index(question);
			fullTextSession.createIndexer(Question.class).threadsForSubsequentFetching(1).threadsToLoadObjects(1).startAndWait();
			fullTextSession.flushToIndexes();
			fullTextSession.getSearchFactory().optimize();
			fullTextSession.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Question> queryByQuestionContent(String question, int maxResults) {
		if (StringUtils.isBlank(question)) {
			return null;
		}
		FullTextSession fullTextSession = Search.getFullTextSession(getSession());
		SearchFactory searchFactory = fullTextSession.getSearchFactory();
		final QueryBuilder queryBuilder = searchFactory.buildQueryBuilder().forEntity(Question.class).get();
		org.apache.lucene.search.Query luceneQuery = queryBuilder.bool().should(queryBuilder.keyword().onField("question").matching(question).createQuery()).createQuery();
		org.hibernate.search.FullTextQuery fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, Question.class).setMaxResults(maxResults);

		List<Question> originalQuestionList = fullTextQuery.list();
		List<Question> questionList = new ArrayList<Question>();
		for (Question entity : originalQuestionList) {
			Question ques = new Question();
			ques.setQuestionId(entity.getQuestionId());
			ques.setQuestion(entity.getQuestion()); 
			questionList.add(ques);
		}

		return questionList;
	}

	@Override
	public List<Answer> queryAnswerByQuestionId(Long questionId) {
		
		return null;
	}

	@Override
	public List<Object[]> getQuestionWithMaxId() {
		Query query = this.getSession().createSQLQuery("SELECT * FROM question WHERE questionId = (SELECT MAX(questionId) FROM question)");
		return query.list();
	}

}
