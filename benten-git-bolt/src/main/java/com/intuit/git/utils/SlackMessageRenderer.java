package com.intuit.benten.git.utils;

import com.intuit.benten.common.actionhandlers.BentenSlackAttachment;
import com.intuit.benten.common.actionhandlers.BentenSlackField;
import com.intuit.benten.common.actionhandlers.BentenSlackResponse;
import com.intuit.benten.common.formatters.SlackFormatter;
import com.intuit.benten.git.model.Assignee;
import com.intuit.benten.git.model.Event;
import com.intuit.benten.git.model.Issue;
import com.intuit.benten.git.model.Label;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author Divakar Ungatla
 * @version 1.0
 */
public class SlackMessageRenderer {
//    public static String renderIssueList(List<Issue> issues){
//        SlackFormatter slackFormatter = SlackFormatter.create();
//        if(issues.isEmpty()){
//            slackFormatter.text("There are no issues assigned to this user currently.");
//        }
//        for(Issue issue:issues){
//            slackFormatter.link(issue.getUrl(),issue.getKey())
//                    .code(issue.getStatus().getName())
//                    .text(issue.getSummary())
//                    .code(issue.getPriority().getName())
//                    .newline();
//        }
//
//        return slackFormatter.build();
//    }

    public static BentenSlackResponse renderIssueDetails(Issue issue) {

    	BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        List<BentenSlackField> bentenSlackFields = new ArrayList<BentenSlackField>();

        List<BentenSlackAttachment> bentenSlackAttachments = new ArrayList<BentenSlackAttachment>();
        BentenSlackAttachment bentenSlackAttachment = new BentenSlackAttachment();
        bentenSlackAttachment.setText("Below are the issue details");
        SlackFormatter slackFormatter = SlackFormatter.create();
        slackFormatter.link(issue.getHtml_url(),issue.getTitle()).newline().newline().text("Summary: "+issue.getBody()).newline().newline()
        					.text("State: "+issue.getState()).newline().newline();
        if(!issue.getAssignees().isEmpty()) {
        		slackFormatter.text("Assignee: ").newline().newline();
        		for(Assignee assignee : issue.getAssignees()) {
        			slackFormatter.text("Name: " + assignee.getLogin()).newline().newline();
        		}
        }else {
        		slackFormatter.text("Assignee: None").newline().newline();
        }
        					


        bentenSlackAttachment.setText(slackFormatter.build());
        bentenSlackAttachments.add(bentenSlackAttachment);

        bentenSlackResponse.setBentenSlackAttachments(bentenSlackAttachments);
        return bentenSlackResponse;

    }

    public static BentenSlackResponse logWorkMessage(String issueKey,String time) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();
        bentenSlackResponse.setSlackText(slackFormatter
                .text("Done! Logged ")
                .code(time)
                .text(" against "+issueKey)
                .build());

        return bentenSlackResponse;

    }

    public static BentenSlackResponse possibleTransitionsMessage(String issueKey,String status, List<String> statuses) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();
        SlackFormatter slackFormatterStatusTransitions = SlackFormatter.create();

        for(String toStatus : statuses){
            slackFormatterStatusTransitions.code(toStatus);
        }
        bentenSlackResponse.setSlackText(slackFormatter
                .text("Issue ")
                .code(issueKey)
                .text(" cannot be moved to ").code(status)
                .newline()
                .bold("Possible transition states: ")
                .newline()
                .text(slackFormatterStatusTransitions.build())
                .build());

        return bentenSlackResponse;

    }

    public static BentenSlackResponse transitionMessage(String issueKey,String status) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();

        bentenSlackResponse.setSlackText(slackFormatter
                .text("Done! Issue ")
                .code(issueKey)
                .text(" was moved to ")
                .bold(status)
                .build());

        return bentenSlackResponse;

    }

    public static BentenSlackResponse commentMessage(String issueKey)  {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
//        SlackFormatter slackFormatter = SlackFormatter.create();
//        try {
//            bentenSlackResponse.setSlackText(slackFormatter
//                    .text("You commented on ")
//                    .link(GitHttpHelper.browseIssueUri(issueKey).toString(), issueKey)
//                    .build());
//        }catch (Exception e){
//            throw new BentenGitException(e.getMessage());
//        }
//
        return bentenSlackResponse;

    }
    
    public static BentenSlackResponse showIssueLabelMessage(String issueUri) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();

        bentenSlackResponse.setSlackText(slackFormatter.text(issueUri)
                .build());

        return bentenSlackResponse;

    }

    public static BentenSlackResponse createIssueMessage(String issueUri) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();

        bentenSlackResponse.setSlackText(slackFormatter.link(issueUri)
                .build());

        return bentenSlackResponse;

    }

    public static BentenSlackResponse errorMessage(String errorMessage) {

        BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
        SlackFormatter slackFormatter = SlackFormatter.create();

        bentenSlackResponse.setSlackText(slackFormatter
                .text("An error occured when I tried to do this operation:")
                .newline()
                .code(errorMessage)
                .build());

        return bentenSlackResponse;

    }

	public static String renderEventsList(List<Event> issueEvents) {
		
		 SlackFormatter slackFormatter = SlackFormatter.create();
	        if(issueEvents.isEmpty()){
	            slackFormatter.text("There are no events assigned to this issue currently.");
	        }
	        for(Event issue:issueEvents){
	        		if (StringUtils.equals(issue.getEvent(), "labeled") || StringUtils.equals(issue.getEvent(), "unlabeled")){
	        			 slackFormatter.text(issue.getEvent() + " issue with label:")
       				  .code(issue.getLabel().getName())
       				  .newline();
	        		}else {
	        			slackFormatter.text(issue.getEvent() + " issue")
	       				  .newline();
	        		}
	           
	        }

	        return slackFormatter.build();
	}
	
	public static String renderLabelsList(List<Label> issueLabels) {
		
		 SlackFormatter slackFormatter = SlackFormatter.create();
	        if(issueLabels.isEmpty()){
	            slackFormatter.text("There are no labels assigned to this issue currently.");
	        }
	        for(Label issue:issueLabels){
	            slackFormatter.text(issue.getName())
	                    .newline();
	        }

	        return slackFormatter.build();
	}

	public static BentenSlackResponse createMyIssuesMessage(String issueList) {
		 BentenSlackResponse bentenSlackResponse= new BentenSlackResponse();
	        SlackFormatter slackFormatter = SlackFormatter.create();

	        bentenSlackResponse.setSlackText(slackFormatter.text(issueList.toString())
	                .build());

	        return bentenSlackResponse;
	}
	
	public static String renderIssuesList(List<Issue> issueLabels) {
		
		 SlackFormatter slackFormatter = SlackFormatter.create();
	        if(issueLabels.isEmpty()){
	            slackFormatter.text("There are no issues assigned to this user currently.");
	        }
	        for(Issue issue:issueLabels){
	            slackFormatter.link(issue.getHtml_url())
	                    .newline();
	        }

	        return slackFormatter.build();
	}


}
