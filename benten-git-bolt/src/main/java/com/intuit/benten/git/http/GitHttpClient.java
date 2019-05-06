package com.intuit.benten.git.http;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.intuit.benten.git.converters.GitConverter;
import com.intuit.benten.git.exceptions.BentenGitException;
import com.intuit.benten.git.model.Event;
import com.intuit.benten.git.model.Issue;
import com.intuit.benten.git.model.JiraError;
import com.intuit.benten.git.model.JiraIssue;
import com.intuit.benten.git.model.Label;
import com.intuit.benten.git.model.Project;
import com.intuit.benten.git.model.Transition;
import com.intuit.benten.git.http.BentenGitHttpClient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Divakar Ungatla
 * @version 1.0
 */
@Component
public class GitHttpClient extends BentenGitHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(GitHttpClient.class);




    public void updateIssue(String issueKey,Map<String,Object> fields){

        try {
            HttpPut httpPut = new HttpPut(GitHttpHelper.issueUri(issueKey));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fields",fields);
            StringEntity stringEntity = formPayload(jsonObject);
            stringEntity.setContentType("application/json");
            httpPut.setEntity(stringEntity);
            HttpResponse httpResponse = request(httpPut);
            if(httpResponse.getStatusLine().getStatusCode()!=204){
                handleGitException(httpResponse);
            }

        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void logWork(String issueKey,JSONObject jsonObject){

        try {
            HttpPost httpPost = new HttpPost(GitHttpHelper.logWorkUri(issueKey));
            StringEntity stringEntity = formPayload(jsonObject);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = request(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()!=201){
                handleGitException(httpResponse);
            }

        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void transitionIssue(String issueKey,JSONObject jsonObject){

        try {
            HttpPost httpPost = new HttpPost(GitHttpHelper.possibleTransitionsUri(issueKey));
            StringEntity stringEntity = formPayload(jsonObject);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = request(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()!=204){
                handleGitException(httpResponse);
            }

        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }



    public void comment(String issueKey,JSONObject jsonObject){

        try {
            HttpPost httpPost = new HttpPost(GitHttpHelper.commentUri(issueKey));
            StringEntity stringEntity = formPayload(jsonObject);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = request(httpPost);
            if(httpResponse.getStatusLine().getStatusCode()!=201){
                handleGitException(httpResponse);
            }

        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private StringEntity formPayload(JSONObject jsonObject) throws JsonProcessingException {
        String payload;
        StringEntity stringEntity;
        payload =  GitConverter.objectMapper.writeValueAsString(jsonObject);
        stringEntity = new StringEntity(payload, "UTF-8");
        stringEntity.setContentType("application/json");
        return stringEntity;
    }


    private void handleGitException(HttpResponse httpResponse) throws IOException {
        String json = EntityUtils.toString(httpResponse.getEntity());
        JiraError jiraError = GitConverter.jiraObjectMapper.readValue(json, JiraError.class);
        JSONObject error = jiraError.getErrors();
        if(error.size()>0) {
            String firstKey = (String) error.keys().next();
            throw new BentenGitException( error.getString(firstKey));
        }else{
            throw new BentenGitException( jiraError.getErrorMessages().get(0));
        }

    }

	public List<Event> getEventsByIssue(String org, String repo, Integer issueNum) {
		List<Event> events;
		try {
			HttpGet httpGet = new HttpGet(GitHttpHelper.getEventsByIssueUri(org, repo, issueNum));
			HttpResponse httpResponse = request(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new BentenGitException(httpResponse.getStatusLine().getReasonPhrase());
			}
			String json = EntityUtils
                .toString(httpResponse.getEntity());
			events = GitConverter.objectMapper.readValue(json, new TypeReference<List<Event>>(){});
		}catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			throw new RuntimeException(ex);
		}
        return events;
	}

	public String addLabelsToIssue(JsonArray labelsArray, String org, String repo, Integer issueNum) {
		
		try {
			 HttpPost httpPost = new HttpPost(GitHttpHelper.addLabelsToIssueUri(org, repo, issueNum));
			 StringEntity stringEntity = new StringEntity(labelsArray.toString());
			 stringEntity.setContentType("application/json");
			 httpPost.setEntity(stringEntity);
			 HttpResponse httpResponse = request(httpPost);
	         if(httpResponse.getStatusLine().getStatusCode()!=200){
	            		handleGitException(httpResponse);
	         }
	         String json = EntityUtils.toString(httpResponse.getEntity());
	         logger.info(json);
	         return "Issue " + issueNum.intValue() + " is labeled successfully." ;
		}catch(Exception ex){
			logger.error(ex.getMessage());
           throw new RuntimeException(ex);
   }
	}

	public String replaceLabelsToIssue(JsonArray labelsArray, String org, String repo, Integer issueNum) {
		
		try {
			 JsonParser jsonParser = new JsonParser();
			 HttpPut httpPut = new HttpPut(GitHttpHelper.addLabelsToIssueUri(org, repo, issueNum));
			 StringEntity stringEntity = new StringEntity(labelsArray.toString());
			 stringEntity.setContentType("application/json");
			 httpPut.setEntity(stringEntity);
			 HttpResponse httpResponse = request(httpPut);
	         if(httpResponse.getStatusLine().getStatusCode()!=200){
	        	 		logger.info(httpResponse.getEntity().toString());
	            		handleGitException(httpResponse);
	         }
	         String json = EntityUtils.toString(httpResponse.getEntity());
	         logger.info(json);
	         return "Issue " + issueNum.intValue() + " is labeled successfully." ;
		}catch(Exception ex){
          throw new RuntimeException(ex);
  }
	}

	public List<Label> getLabelsByIssue(String org, String repo, Integer issueNum) {
		List<Label> labels;
		try {
			HttpGet httpGet = new HttpGet(GitHttpHelper.getLabelsByIssueUri(org, repo, issueNum));
			HttpResponse httpResponse = request(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new BentenGitException(httpResponse.getStatusLine().getReasonPhrase());
			}
			String json = EntityUtils
                .toString(httpResponse.getEntity());
			labels = GitConverter.objectMapper.readValue(json, new TypeReference<List<Label>>(){});
			
		}catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
			throw new RuntimeException(ex);
		}
        return labels;
	}
	




}
