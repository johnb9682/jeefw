package com.jeefw.controller.sys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeefw.core.Constant;
import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.Answer;
import com.jeefw.model.sys.Question;
import com.jeefw.model.sys.QuestionAndAnswer;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.AnswerService;
import com.jeefw.service.sys.QuestionAndAnswerService;
import com.jeefw.service.sys.QuestionService; 


/**
 * @author John
 * @Slack Redirect Response
 */
@Controller
@RequestMapping("/question")
public class SlackResponseController extends JavaEEFrameworkBaseController<SysUser> implements Constant { 

	@Resource
	private QuestionService questionService;
	@Resource
	private QuestionAndAnswerService questionAndAnswerService;
	@Resource
	private AnswerService answerService;
	
	int MAX_RECORD_SHOWN = 3;
	String SLACK_DIALOG_OPEN_URL = "https://slack.com/api/dialog.open"; 
	
	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();  
		result.put("result", 200);
		// questionAndAnswerService.deleteByProperties("answerId", Long.valueOf(61));
		writeJSON(response, result);
	}
	
	@RequestMapping(value = "/input", method = { RequestMethod.POST})
	public void input(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();  
		String inputText = request.getParameter("text");
//		result.put("text", inputText);
//		result.put("other", "0.4.6 ");
//		result.put("working pregress", "create function Return valid response to slack");
//		result.put("other", "0.5.9 ");
//		result.put("working pregress", "delete function with correct response show to user");
		result.put("other", "0.6.5 ");
		result.put("working pregress", "Edit function");
//		result.put("working pregress", "Edit function provide recegnized response");
//		result.put("other", "0.7.0 ");
//		result.put("working pregress", "Clean up"); 
//		result.put("working pregress", "Clean up Delete System out. prepare log class"); 
//		result.put("working pregress", "Clean up Provide a staic class contain all printout text"); 
//		delete system
//	    update function in service layer
		response.setStatus(HttpServletResponse.SC_OK);
		writeJSON(response, result);
	}
	
	@RequestMapping(value = "/ask", method = { RequestMethod.POST})
	public void ask(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("Coming into the ask controller"); 
		String payload = request.getParameter("payload");
		System.out.println("payload: " + payload);
		JSONObject result = new JSONObject(); 
		if(payload != null && !payload.isEmpty()) {
			/* 
			 * This is a interactive_message. Either delete / edit 
			 */
//			try {
//				JSONParser parser = new JSONParser();
//				org.json.simple.JSONObject jsonPayload = (org.json.simple.JSONObject) parser.parse(payload);
//				org.json.simple.JSONArray payloadActions = (org.json.simple.JSONArray) jsonPayload.get("actions");
//				System.out.println(payloadActions);
//				org.json.simple.JSONObject payloadActionObject = (org.json.simple.JSONObject) payloadActions.get(0);
//				System.out.println(payloadActionObject);
//				String actionName = (String) payloadActionObject.get("name");
//				System.out.println(actionName);
//				switch (actionName) {
//					case "edit":
//						interactiveMessageEdit(result, jsonPayload);
//						return;  
//					case "delete":
//						interactiveMessageDelete(result, jsonPayload);
//						break;
//				} 
//				String message = result.toString();
//				// response.getWriter().write(message); 
//				// return;
//			} catch (ParseException e) { 
//				e.printStackTrace();
//			}   
		} else {
			JSONArray attachments = new JSONArray();  
			String inputText = request.getParameter("text"); 
			if(inputText == null || inputText.isEmpty()) { 
				result.put("text", "Input is empty. Please input question after command.");
				String message = result.toString(); 
			} else {
				System.out.println("Text:" + inputText );
				List<Question> questionList = questionService.queryByQuestionContent(inputText);
				System.out.println("questionList:" + questionList );
				if(questionList.size() > 0) {
					for(int i = 0 ;i < questionList.size() && i < MAX_RECORD_SHOWN; i ++ ) { 
						Long questionId = questionList.get(i).getQuestionId();
						QuestionAndAnswer questionAndAnswer = questionAndAnswerService.queryByQuestionId(questionId);
						System.out.println("questionAndAnswer:" + questionAndAnswer );
						if(questionAndAnswer != null) { 
							System.out.println("getAnswerId:" + questionAndAnswer.getAnswerId() );
							Answer answer = answerService.getAnswer(questionAndAnswer.getAnswerId());
							if(answer != null) {   
								attachments.add(generateAnswer(answer, questionAndAnswer, questionList.get(i)));
								System.out.println("Answer:" + answer.getAnswer() );
								System.out.println("attachments:" + attachments );
							}
							if(attachments.size() == 3) {
								break;
							}
						} 
					} 
					result.put("attachments", attachments);
				} else {
					result.put("text", "No answer matches your question. Please change your question.");
				}
			}
		} 
		
		result.put("username", "Conan");
		result.put("mrkdwn", true); 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		String message = result.toString();
		response.getWriter().write(message); 
		System.out.println("Finish the ask controller");
	}
	
	public JSONObject generateAnswer(Answer answer, QuestionAndAnswer questionAndAnswer, Question question){
		JSONObject answerJson = new JSONObject();  
		answerJson.put("text", answer.getAnswer()); 
		answerJson.put("fields", answerService.generateFieldWithAuthorAndDate(answer.getAuthor(), answer.getDate()));
		answerJson.put("color", "good");
		answerJson.put("title", question.getQuestion());
		answerJson.put("callback_id", "update");
		JSONArray actions = new JSONArray();  
		JSONObject editAction = new JSONObject(); 
		editAction.put("name", "edit");
		editAction.put("text", "Edit answer");
		editAction.put("type", "button");
//		JSONObject editActionValue = new JSONObject(); 
//		editActionValue.put("answerId", questionAndAnswer.getAnswerId());
//		editActionValue.put("questoinAnswerId", questionAndAnswer.getQuestionAnswerId());
//		editActionValue.put("questoinId", questionAndAnswer.getQuestionId());
//		editAction.put("value", editActionValue.toString());
		editAction.put("value", questionAndAnswer.getAnswerId());
		actions.add(editAction);
		JSONObject deleteAction = new JSONObject();
		deleteAction.put("name", "delete");
		deleteAction.put("text", "Delete answer");
		deleteAction.put("type", "button");
		JSONObject deleteConfirmDialog = new JSONObject();
		deleteConfirmDialog.put("title", "Confirmation");
		deleteConfirmDialog.put("text", "Are you sure you want to delete this answer? This action can not be undone.");
		deleteConfirmDialog.put("ok_text", "Delete");
		deleteConfirmDialog.put("dismiss_text", "Cancel");
		deleteAction.put("confirm", deleteConfirmDialog);
//		JSONObject deleteActionValue = new JSONObject(); 
//		deleteActionValue.put("answerId", questionAndAnswer.getAnswerId());
//		deleteActionValue.put("questoinAnswerId", questionAndAnswer.getQuestionAnswerId());
//		deleteActionValue.put("questoinId", questionAndAnswer.getQuestionId());
//		deleteAction.put("value", deleteActionValue); 
		deleteAction.put("value", questionAndAnswer.getAnswerId());
		actions.add(deleteAction);
		answerJson.put("actions", actions);
		return answerJson;
	}
	
	/*
	 * This is used to respond to the interactive message delete button
	 * get answerId from value
	 * check question_answer table. 
	 * delete the question if it has the only question
	 */
	public void interactiveMessageDelete (JSONObject result, org.json.simple.JSONObject jsonPayload) {
		System.out.println("jsonPayload: " + jsonPayload); 
		org.json.simple.JSONArray payloadActions = (org.json.simple.JSONArray) jsonPayload.get("actions"); 
		org.json.simple.JSONObject payloadActionObject = (org.json.simple.JSONObject) payloadActions.get(0); 
		String targetAnswerId = (String) payloadActionObject.get("value");
		System.out.println("Edit question for questionId: " + targetAnswerId); 
		Long answerId = Long.valueOf (targetAnswerId) ;
		System.out.println("Edit question for questionId: " + answerId); 
		boolean deleteAnswerResult = answerService.deleteByPK(answerId);
		System.out.println("Delete Answer: " + deleteAnswerResult);
		List<QuestionAndAnswer> questionAndAnswerList = questionAndAnswerService.queryByProerties("answerId", answerId); 
		for(ListIterator<QuestionAndAnswer> iter = questionAndAnswerList.listIterator(); iter.hasNext();){ 
			QuestionAndAnswer questionAndAnswer = iter.next();    
			questionAndAnswerService.deleteByPK(questionAndAnswer.getQuestionAnswerId());
			questionService.deleteByPK(questionAndAnswer.getQuestionId());
		}
		// questionAndAnswerService.deleteByProperties("answerId", answerId);
		result.put("text", "Successful delete one answer. Please ask question again.");
	}
	/*
	 * This is used to respond to the interactive message edit button
	 */
	public void interactiveMessageEdit (JSONObject result, org.json.simple.JSONObject jsonPayload) {
		System.out.println("jsonPayload: " + jsonPayload);
		org.json.simple.JSONArray payloadActions = (org.json.simple.JSONArray) jsonPayload.get("actions"); 
		org.json.simple.JSONObject payloadActionObject = (org.json.simple.JSONObject) payloadActions.get(0); 
		String targetAnswerId = (String) payloadActionObject.get("value");
		Long answerId = Long.valueOf (targetAnswerId) ;
		Answer answer = answerService.get(answerId);
		System.out.println("Edit answer for answerId: " + answerId);
		
		if(answer == null) {
			return;
		}
		
		// Send a post request to slack to open the dialog 
	
		// result.put("result", 200);
		String trigger_id = (String) jsonPayload.get("trigger_id");
		String token = (String) jsonPayload.get("token"); 
		
		JSONObject dialog = new JSONObject();
		// I have no choice but doing this ugly code. 
		// slack submission does not allow me to pass customized parameter
		dialog.put("callback_id", "editanswersubmit" + answer.getAnswerId());
		dialog.put("title", "Edit the answer");
		JSONArray elements = new JSONArray();
		 
		JSONObject elementAnswer = new JSONObject(); 
		elementAnswer.put( "type", "textarea");
		elementAnswer.put("label", "Edit Answer");
		elementAnswer.put("name", "answer"); // will be used for create request, included in the payload
		elementAnswer.put("hint", "Edit answer.");
		elementAnswer.put("value", answer.getAnswer());
		elements.add(elementAnswer);
		dialog.put("elements", elements);  
		
		try {
			sendPost(trigger_id, getJsonObject(dialog));
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}

	@RequestMapping(value = "/create", method = { RequestMethod.POST})
	public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
		System.out.println("create");
		Map<String, Object> result = new HashMap<String, Object>();
		String inputQuestion = request.getParameter("question");
		String inputAnswer = request.getParameter("answer");
		String inputAuthor = request.getParameter("user_name"); 
		Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()); 
		System.out.println(inputQuestion + "|"+ inputAnswer + "|" + inputAuthor+  "|" + currentDate);
		if(inputQuestion != null && inputAnswer != null ) {
			// && !StringUtils.isEmpty(inputQuestion) && !StringUtils.isEmpty(inputAnswer) 
			System.out.println("start to create");
			Answer answer = new Answer();  
			answer.setAnswer(inputAnswer);
			answer.setAuthor(inputAuthor);
			answer.setDate(currentDate); 
			answerService.persist(answer);
			Long maxAnswerId = answerService.getMaxAnswerId(); 
			System.out.println("answer: " + answer);
			Question question = new Question(); 
			question.setQuestion(inputQuestion);
			questionService.persist(question);
			Long maxQuestionId = questionService.getMaxQuestionId(); 
			System.out.println("question: " + question);
			QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer();
			questionAndAnswer.setAnswerId(maxAnswerId);
			questionAndAnswer.setQuestionId(maxQuestionId);
			questionAndAnswerService.persist(questionAndAnswer);
			System.out.println("questionAndAnswerService: " + questionAndAnswer);
			result.put("answer", answer);
			result.put("question", question);
			result.put("ok", true);
		} 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		String message = result.toString();
		response.getWriter().write(message); 
		
//		response.setStatus(HttpServletResponse.SC_OK);
//		writeJSON(response, result);
	}
	
	/* 
	 * all the parameter below are inside payload
	 * type - to differentiate from other interactive components, look for the string value dialog_submission.
	 * submission - a hash of key/value pairs representing the user's submission. Each key is a name field your app provided when composing the form. Each value is the user's submitted value, or in the case of a select menu, the value you assigned to a specific response.
	 * callback_id - this value is the unique callback_id identifier your app gave this instance of the dialog.
	 * team - this simple hash contains the id and name of the workspace from which this interaction occurred
	 * user - this simple hash contains the id and name of the user who completed the form
	 * channel - this simple hash contains the id and name of the channel or conversation where this dialog was completed
	 * action_ts - this is a unique identifier for this specific action occurrence generated by Slack. It can be evaluated as a timestamp with milliseconds if that is helpful to you.
	 * token - the verification token shared between your app and Slack used to validate an incoming request originates from Slack.
	 */
	@RequestMapping(value = "/update", method = { RequestMethod.POST})
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		System.out.println("Enter update controller");
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		// result.put("result", 200);
		String payload = request.getParameter("payload");
		System.out.println("Payload: " + payload); 
		JSONObject result = new JSONObject(); 
		JSONParser parser = new JSONParser();
		try {
			org.json.simple.JSONObject jsonPayload = (org.json.simple.JSONObject) parser.parse(payload);   
			System.out.println("Able to parse JSON: " + jsonPayload);
			String callback_id = (String) jsonPayload.get("callback_id"); 
			
			switch(callback_id) {
			case "create":
				String inputAuthor = (String) ((org.json.simple.JSONObject)jsonPayload.get("user")).get("name");
				String inputQuestion = (String) ((org.json.simple.JSONObject)jsonPayload.get("submission")).get("question");
				String inputAnswer = (String) ((org.json.simple.JSONObject)jsonPayload.get("submission")).get("answer"); 
				result = createQuestionAndAnswer(inputAuthor, inputQuestion, inputAnswer);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.setStatus(HttpServletResponse.SC_OK);
				result.put("result", 200);
				result.put("ok", true);
				String message = result.toString(); 
				response.setContentType("text/html");
				response.setStatus(HttpServletResponse.SC_OK);
				return; 
			case "update":
				org.json.simple.JSONArray payloadActions = (org.json.simple.JSONArray) jsonPayload.get("actions");
				System.out.println(payloadActions);
				org.json.simple.JSONObject payloadActionObject = (org.json.simple.JSONObject) payloadActions.get(0);
				System.out.println(payloadActionObject);
				String actionName = (String) payloadActionObject.get("name");
				System.out.println(actionName); 
				switch(actionName){
				case "edit":
					interactiveMessageEdit(result, jsonPayload);
					return;  
				case "delete":
					interactiveMessageDelete(result, jsonPayload);
					break;
				} 
				break; 
			default: 
				if(callback_id.contains("editanswersubmit")){
					Long editAnswerId = Long.valueOf(callback_id.replace("editanswersubmit", ""));
					System.out.println("Answer Id: " + editAnswerId);
					System.out.println("Origin Answer: " + answerService.get(editAnswerId).getAnswer());
					String editAnswer = (String) ((org.json.simple.JSONObject)jsonPayload.get("submission")).get("answer"); 
					System.out.println("Answer from submission " + editAnswer);
					answerService.updateByProperties("answerId", editAnswerId, "answer", editAnswer);
					System.out.println("Updated Answer: " + answerService.get(editAnswerId).getAnswer());
//					mapResponse.put("ok", true);  
//					response.setStatus(HttpServletResponse.SC_OK);
					response.setContentType("text/html");
					response.setStatus(HttpServletResponse.SC_OK);
					return;
				}
				break;
			}
			 
			
			
		} catch (ParseException e) { 
			e.printStackTrace();
		}  
		
		result.put("username", "Conan");
		result.put("mrkdwn", true); 
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		String message = result.toString();
		response.getWriter().write(message); 
		System.out.println("Finish the update controller");
//		response.setStatus(HttpServletResponse.SC_OK);
//		writeJSON(response, result);
	} 
	

	public JSONObject createQuestionAndAnswer(String inputAuthor, String inputQuestion, String inputAnswer ) {
		JSONObject result = new JSONObject();
		Date currentDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()); 
		System.out.println(inputQuestion + "|"+ inputAnswer + "|" + inputAuthor+  "|" + currentDate);
		if(inputQuestion != null && inputAnswer != null ) {
			System.out.println("start to create");
			Answer answer = new Answer();  
			answer.setAnswer(inputAnswer);
			answer.setAuthor(inputAuthor);
			answer.setDate(currentDate); 
			answerService.persist(answer);
			Long maxAnswerId = answerService.getMaxAnswerId(); 
			System.out.println("answer: " + answer);
			Question question = new Question(); 
			question.setQuestion(inputQuestion);
			questionService.persist(question);
			Long maxQuestionId = questionService.getMaxQuestionId(); 
			System.out.println("question: " + question);
			QuestionAndAnswer questionAndAnswer = new QuestionAndAnswer();
			questionAndAnswer.setAnswerId(maxAnswerId);
			questionAndAnswer.setQuestionId(maxQuestionId);
			questionAndAnswerService.persist(questionAndAnswer);
			System.out.println("questionAndAnswerService: " + questionAndAnswer);
			result.put("answer", answer);
			result.put("question", question);
		}
		
		return result;
	}
	
	/*
	 * Available parameter for request
	 * token=gIkuvaNzQIHg97ATvDxqgjtO
	 * team_id=T0001
	 * team_domain=example
	 * enterprise_id=E0001
	 * enterprise_name=Globular%20Construct%20Inc
	 * channel_id=C2147483705
	 * channel_name=test
	 * user_id=U2147483697
	 * user_name=Steve
	 * command=/weather
	 * text=94070
	 * response_url=https://hooks.slack.com/commands/1234/5678
	 * trigger_id=13345224609.738474920.8088930838d88f008e0
	 */
	@RequestMapping(value = "/opendialog", method = { RequestMethod.POST})
	public void opendialog(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		Map<String, Object> result = new HashMap<String, Object>();
		// result.put("result", 200);
		String trigger_id = request.getParameter("trigger_id");
		String token = request.getParameter("token"); 
		
		JSONObject dialog = new JSONObject();
		dialog.put("callback_id", "create");
		dialog.put("title", "New question and answer");
		JSONArray elements = new JSONArray();
		JSONObject elementQuestion = new JSONObject(); 
		elementQuestion.put( "type", "text");
		elementQuestion.put("label", "Question");
		elementQuestion.put("name", "question"); // will be used for create request, included in the payload
		elements.add(elementQuestion);
		JSONObject elementAnswer = new JSONObject(); 
		elementAnswer.put( "type", "textarea");
		elementAnswer.put("label", "Answer");
		elementAnswer.put("name", "answer"); // will be used for create request, included in the payload
		elementAnswer.put("hint", "Provide answer for your question.");
		elements.add(elementAnswer);
		dialog.put("elements", elements);  
		
		try {
			sendPost(trigger_id, getJsonObject(dialog));
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void openDialogEdit(Long answerId) {
		
	}
	
	// HTTP POST request
	// Apache HttpClient
	private void sendPost(String trigger_id, String dialog) {

		String url = "https://slack.com/api/dialog.open";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);

		// add header 
		post.addHeader("User-Agent", "Mozilla/5.0"); 
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Authorization", "Bearer xoxa-69335041190-321290058903-321290059191-992bed1f9cd43ccd948fbb468f955542");
		post.addHeader("Accept-Charset", "UTF-8"); 

		List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
		urlParameters.add(new BasicNameValuePair("trigger_id", trigger_id));
		urlParameters.add(new BasicNameValuePair("dialog", dialog)); 
		
		try {
			post.setEntity(new UrlEncodedFormEntity(urlParameters));  
			HttpResponse response = client.execute(post);
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + post.getEntity());
			System.out.println("Response Code : " +
	                                    response.getStatusLine().getStatusCode());

			BufferedReader rd = new BufferedReader(
	                        new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}

			System.out.println(result.toString());
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	@JsonRawValue 
    @JsonProperty(value = "jsonString")
    public String getJsonObject(JSONObject jsonObject)
    {
         return jsonObject.toString();
    } 
	
}
