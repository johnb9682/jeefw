package com.jeefw.service.sys;

import java.util.List;

import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Question;

import core.service.Service;

/**
 * @author John
 */
public interface QuestionService extends Service<Question> { 

	void indexingQuestion();

	List<Question> queryByQuestionContent(String question);
	
	Answer queryAnswerByQuestionId(Long questionId);
	
	Long getMaxQuestionId();

}
