package com.intuit.benten.git.actionhandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.intuit.benten.common.actionhandlers.BentenActionHandler;
import com.intuit.benten.common.actionhandlers.BentenHandlerResponse;
import com.intuit.benten.common.annotations.ActionHandler;
import com.intuit.benten.common.constants.SlackConstants;
import com.intuit.benten.common.helpers.BentenMessageHelper;
import com.intuit.benten.common.nlp.BentenMessage;
import com.intuit.benten.git.BentenGitClient;
import com.intuit.benten.git.exceptions.BentenGitException;
import com.intuit.benten.git.utils.SlackMessageRenderer;

import net.sf.json.JSONObject;

@Component
@ActionHandler(action = GitActions.ACTION_GITHUB_ADD_LABEL)
public class GitAddLabelsToIssuesActionHandler implements BentenActionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(GitCreateIssueActionHandler.class);

    @Autowired
    private BentenGitClient bentenGitClient;

    public BentenHandlerResponse handle(BentenMessage bentenMessage) {
    	
        String org = BentenMessageHelper.getParameterAsString(bentenMessage, SlackConstants.ORG);
        String repo = BentenMessageHelper.getParameterAsString(bentenMessage, SlackConstants.REPO);
        Integer issueNum = BentenMessageHelper.getParameterAsInteger(bentenMessage, SlackConstants.ISSUE_NUMBER);
        String labels = BentenMessageHelper.getParameterAsString(bentenMessage, SlackConstants.LABELS);

        BentenHandlerResponse bentenHandlerResponse = new BentenHandlerResponse();

        try {
            
        		List<String> labelList = new ArrayList<String>(Arrays.asList(labels.split(",")));
        		JsonArray labelsArray = new JsonArray();
        		for(String label : labelList) {
        			labelsArray.add(label);
        		}
        		logger.info("*********" + labelsArray.toString());
            String issueUri = bentenGitClient.addLabelsToIssue(labelsArray, org, repo, issueNum);

            bentenHandlerResponse.setBentenSlackResponse(SlackMessageRenderer.showIssueLabelMessage(issueUri));

        }catch(BentenGitException e){
            bentenHandlerResponse
                    .setBentenSlackResponse(SlackMessageRenderer.errorMessage(e.getMessage().substring(e.getMessage().lastIndexOf(":")+1)));
        }catch (Exception e){
        		logger.error(e.getMessage());
            throw new BentenGitException(e.getMessage());
        }

        return bentenHandlerResponse;
    }

}
