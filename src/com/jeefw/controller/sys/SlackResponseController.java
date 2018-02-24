package com.jeefw.controller.sys;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Controller;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonRawValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jeefw.core.Constant;
import com.jeefw.core.JavaEEFrameworkBaseController;
import com.jeefw.model.sys.SysUser;
import com.jeefw.service.sys.QuestionService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject; 


/**
 * @author John
 * @Slack Redirect Response
 */
@Controller
@RequestMapping("/question")
public class SlackResponseController extends JavaEEFrameworkBaseController<SysUser> implements Constant { 

	@Resource
	private QuestionService questionService;
	
	@RequestMapping("/get")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();  
		result.put("result", 200);
		writeJSON(response, result);
	}
	
	@RequestMapping(value = "/ask", method = { RequestMethod.POST})
	public void ask(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		Map<String, Object> result = new HashMap<String, Object>();
		// result.put("result", 200);
		String inputText = request.getParameter("text");
		result.put("text", questionService.queryByQuestionContent(inputText));
		result.put("inputtext", inputText);
		// result.put("username", request.getParameter("user_name"));
		// result.put("mrkdwn", true);
		writeJSON(response, result);
	}

	@RequestMapping(value = "/opendialog", method = { RequestMethod.POST})
	public void opendialog(HttpServletRequest request, HttpServletResponse response) throws IOException {  
		Map<String, Object> result = new HashMap<String, Object>();
		// result.put("result", 200);
		String trigger_id = request.getParameter("trigger_id");
		String token = request.getParameter("token");
		
		JSONObject jsonResponse = new JSONObject();
		jsonResponse.put("token", "xoxa-69335041190-321290058903-321290059191-992bed1f9cd43ccd948fbb468f955542");
		jsonResponse.put("trigger_id", trigger_id);
		JSONObject dialog = new JSONObject();
		dialog.put("callback_id", "opendialog");
		dialog.put("title", "New question and answer");
		JSONArray elements = new JSONArray();
		JSONObject elementQuestion = new JSONObject(); 
		elementQuestion.put( "type", "text");
		elementQuestion.put("label", "Pickup Location");
		elementQuestion.put("name", "loc_origin");
		elements.add(elementQuestion);
		dialog.put("elements", elements);
		jsonResponse.put("dialog", dialog);
		System.out.println(dialog.toString());
		
		response.setStatus(302); //this makes the redirection keep your requesting method as is.
		// response.addHeader("Location", "https://slack.com/api/dialog.open");
		response.setContentType("application/json");     // application/x-www-form-urlencoded
		// response.addHeader("trigger_id", trigger_id);
		response.addHeader("Authorization", "Bearer xoxa-69335041190-321290058903-321290059191-992bed1f9cd43ccd948fbb468f955542");
		
		try {
			sendPost(trigger_id, getJsonObject(dialog));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
//	// HTTP POST request
//	// Java HttpURLConnection
//	private void sendPost(String trigger_id, String dialog) throws Exception {
//
//		String url = "https://slack.com/api/dialog.open";
//		URL obj = new URL(url);
//		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection(); 
//		
//		//add request header
//		con.setRequestMethod("POST");
//		con.setRequestProperty("User-Agent", "Mozilla/5.0"); 
//		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		con.setRequestProperty("Authorization", "Bearer xoxa-69335041190-321290058903-321290059191-992bed1f9cd43ccd948fbb468f955542");
//		con.setRequestProperty("Accept-Charset", "UTF-8"); 
//
//		String urlParameters =  "trigger_id=" + trigger_id
//								+ "&dialog=" + dialog;
//
//		// Send post request
//		con.setDoOutput(true);
//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//		wr.writeBytes(urlParameters);
//		wr.flush();
//		wr.close();
//
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
//		System.out.println("Dialog : " + dialog);
//		System.out.println("Response Code : " + responseCode);
//
//		BufferedReader in = new BufferedReader(
//		        new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
//
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close(); 
//	}
	
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
