package com.intuit.benten.git.actionhandlers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.intuit.benten.common.actionhandlers.BentenActionHandler;
import com.intuit.benten.common.actionhandlers.BentenHandlerResponse;
import com.intuit.benten.common.actionhandlers.BentenSlackResponse;
import com.intuit.benten.common.annotations.ActionHandler;
import com.intuit.benten.common.constants.SlackConstants;
import com.intuit.benten.common.helpers.BentenMessageHelper;
import com.intuit.benten.common.nlp.BentenMessage;
import com.intuit.benten.git.BentenGitClient;
import com.intuit.benten.git.exceptions.BentenGitException;
import com.intuit.benten.git.model.Event;
import com.intuit.benten.git.model.Label;
import com.intuit.benten.git.utils.SlackMessageRenderer;

import net.sf.json.JSONObject;

@Component
@ActionHandler(action = GitActions.ACTION_GITHUB_GET_LABELS)
public class GitGetLabelsByIssueActionHandler implements BentenActionHandler{
	
	 private static final Logger logger = LoggerFactory.getLogger(GitGetLabelsByIssueActionHandler.class);

	    @Autowired
	    private BentenGitClient bentenGitClient;

	    public BentenHandlerResponse handle(BentenMessage bentenMessage) {
	    	
	    		logger.info("*******************************");
	    		logger.info(bentenMessage.toString());
	        Integer issueNum = BentenMessageHelper.getParameterAsInteger(bentenMessage, SlackConstants.ISSUE_NUMBER);
	        String org = BentenMessageHelper.getParameterAsString(bentenMessage, SlackConstants.ORG);
	        String repo = BentenMessageHelper.getParameterAsString(bentenMessage, SlackConstants.REPO);

	        BentenHandlerResponse bentenHandlerResponse = new BentenHandlerResponse();

	        try {

	            List<Label> issueEvents = bentenGitClient.getLabelsByIssue(org, repo, issueNum);

	            //bentenHandlerResponse.setBentenSlackResponse(SlackMessageRenderer.createIssueMessage(issueUri));
	            BentenSlackResponse bentenSlackResponse = new BentenSlackResponse();
	            bentenSlackResponse.setSlackText(SlackMessageRenderer.renderLabelsList(issueEvents));
	            bentenHandlerResponse.setBentenSlackResponse(bentenSlackResponse);
	        }catch(BentenGitException e){
	            bentenHandlerResponse
	                    .setBentenSlackResponse(SlackMessageRenderer.errorMessage(e.getMessage().substring(e.getMessage().lastIndexOf(":")+1)));
	        }catch (Exception e){
	            throw new BentenGitException(e.getMessage());
	        }

	        return bentenHandlerResponse;
	    }

}
