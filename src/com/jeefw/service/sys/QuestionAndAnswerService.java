package com.jeefw.service.sys;
 
import com.jeefw.model.sys.QuestionAndAnswer; 

import core.service.Service;

public interface QuestionAndAnswerService extends Service<QuestionAndAnswer> {
	QuestionAndAnswer queryByQuestionId(Long questionId);

}
