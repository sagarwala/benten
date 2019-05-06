package com.intuit.benten.git;

import com.google.gson.JsonArray;
import com.intuit.benten.git.exceptions.BentenGitException;
import com.intuit.benten.git.http.GitHttpClient;
import com.intuit.benten.git.model.Event;
import com.intuit.benten.git.model.Issue;
import com.intuit.benten.git.model.JiraIssue;
import com.intuit.benten.git.model.Label;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.annotation.PostConstruct;


/**
 * @author Divakar Ungatla
 * @version 1.0
 */
@Component
public class BentenGitClient {

    private static final Logger logger = LoggerFactory.getLogger(BentenGitClient.class);

    @Autowired
    private GitHttpClient gitHttpClient;


    @PostConstruct
    public void init(){
        try {


        }
        catch (Exception ex){
            logger.error(ex.getMessage(),ex);
        }
    }

    public String createIssue(JSONObject fields, String org, String repo){
        try {
            return gitHttpClient.createIssue(fields, org, repo);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
    }

	public List<Event> getEventsByIssue(String org, String repo, Integer issueNum) {
		
		try {
            return gitHttpClient.getEventsByIssue(org, repo, issueNum);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}

	public String addLabelsToIssue(JsonArray labelsArray, String org, String repo, Integer issueNum) {
		try {
            return gitHttpClient.addLabelsToIssue(labelsArray, org, repo, issueNum);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}

	public String replaceLabelsToIssue(JsonArray labelsArray, String org, String repo, Integer issueNum) {
		try {
            return gitHttpClient.replaceLabelsToIssue(labelsArray, org, repo, issueNum);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}

	public List<Label> getLabelsByIssue(String org, String repo, Integer issueNum) {
		try {
            return gitHttpClient.getLabelsByIssue(org, repo, issueNum);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}
	
	public Issue getIssueDetails(String org, String repo, Integer issueNum){
        try {
            Issue issue = gitHttpClient.getIssueDetails(org, repo, issueNum);
            return issue;
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
    }

    public String addIssueAssignee(JSONObject fields, String org, String repo, Integer issueNum){
        try {
            String issue = gitHttpClient.addIssueAssignee(fields, org, repo, issueNum);
            return issue;
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
    }

	public JiraIssue getJiraIssueDetails(String jiraTicketId, String expandedFields) {
		
		try {
            JiraIssue issue = gitHttpClient.getJiraIssueDetails(jiraTicketId,expandedFields);
            return issue;
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}

	public List<Issue> myIssues(String user) {
		try {
            return gitHttpClient.myIssues(user);
        }
        catch(Exception ex){
            logger.error(ex.getMessage(),ex);
            throw new BentenGitException(ex.getMessage());
        }
	}








}
