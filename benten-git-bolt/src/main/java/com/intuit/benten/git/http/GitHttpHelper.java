package com.intuit.benten.git.http;

import org.apache.http.client.utils.URIBuilder;

import com.intuit.benten.git.properties.GitProperties;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Divakar Ungatla
 * @version 1.0
 */
public class GitHttpHelper{

    public static String getBaseUri() {
        return GitProperties.baseurl+"/api/v3/";
    }

    public static URI buildURI(String path, Map<String, String> params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setPath(path);
        if (params != null) {
            Iterator var4 = params.entrySet().iterator();

            while(var4.hasNext()) {
                Map.Entry<String, String> ent = (Map.Entry)var4.next();
                uriBuilder.addParameter((String)ent.getKey(), (String)ent.getValue());
            }
        }

        return uriBuilder.build();
    }

    public static URI issueUri(String issueKey) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();
        URI searchUri = buildURI(getBaseUri() + "issue/"+issueKey, queryParams);
        return searchUri;
    }

    public static URI logWorkUri(String issueKey) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();
        URI searchUri = buildURI(getBaseUri() + "issue/"+issueKey+"/worklog", queryParams);
        return searchUri;
    }

    public static URI possibleTransitionsUri(String issueKey) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();
        URI searchUri = buildURI(getBaseUri() + "issue/"+issueKey+"/transitions", queryParams);
        return searchUri;
    }

    public static URI commentUri(String issueKey) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();
        URI searchUri = buildURI(getBaseUri() + "issue/"+issueKey+"/comment", queryParams);
        return searchUri;
    }

    public static URI metaDataUri(Map queryParams) throws URISyntaxException {
        URI metaDataUri = buildURI(getBaseUri() + "issue/createmeta", queryParams);
        return metaDataUri;
    }


    public static URI createIssueUri(String org, String repo) throws URISyntaxException {
        URI createIssueuri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues",null);
        return createIssueuri;
    }

    public static URI issueDetailsUri(String issueKey, String expandFields) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();

        if (expandFields != null) {
            queryParams.put("expand", expandFields);
        }

        URI searchUri = buildURI(getBaseUri() + "issue/"+issueKey, queryParams);
        return searchUri;
    }

    public static URI createSearchUri(String jql, String includedFields, String expandFields, Integer maxResults, Integer startAt) throws URISyntaxException {
        Map<String, String> queryParams = new HashMap();
        queryParams.put("jql", jql);
        if (maxResults != null) {
            queryParams.put("maxResults", String.valueOf(maxResults));
        }

        if (includedFields != null) {
            queryParams.put("fields", includedFields);
        }

        if (expandFields != null) {
            queryParams.put("expand", expandFields);
        }

        if (startAt != null) {
            queryParams.put("startAt", String.valueOf(startAt));
        }

        URI searchUri = buildURI(getBaseUri() + "search", queryParams);
        return searchUri;
    }

	public static URI getEventsByIssueUri(String org, String repo, Integer issueNum) throws URISyntaxException {
		URI issueEventsUri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues/" + issueNum.intValue() + "/events",null);
        return issueEventsUri;
	}

	public static URI addLabelsToIssueUri(String org, String repo, Integer issueNum) throws URISyntaxException {
		 URI addLabelsToIssueUri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues/" + issueNum.intValue() + "/labels",null);
	     return addLabelsToIssueUri;
	}

	public static URI getLabelsByIssueUri(String org, String repo, Integer issueNum) throws URISyntaxException {
		URI getLabelsByIssueUri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues/" + issueNum.intValue() + "/labels",null);
	    return getLabelsByIssueUri;
	}
	
	//POST /repos/:owner/:repo/issues/:number/assignees
    public static URI addIssueAssigneeUri(String org, String repo, Integer issueNum) throws URISyntaxException {
        URI addIssueAssigneeUri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues/"+issueNum.intValue()+"/assignees",null);
        return addIssueAssigneeUri;
    }
    
    public static URI getIssueDetailsUri(String org, String repo, Integer issueNum) throws URISyntaxException {
        URI getIssueDetailsuri = buildURI(getBaseUri()+"repos/"+org+"/" + repo + "/issues"+"/"+issueNum.intValue(),null);
        return getIssueDetailsuri;
    }


	public static URI jiraIssueUri(String issueKey) throws URISyntaxException {
		        Map<String, String> queryParams = new HashMap();
		        URI searchUri = buildURI("https://jira.intuit.com/rest/api/2/issue/"+issueKey, queryParams);
		        return searchUri;
	}

	public static URI jiraIssueDetailsUri(String jiraTicketId, String expandFields) throws URISyntaxException {
		Map<String, String> queryParams = new HashMap();

        if (expandFields != null) {
            queryParams.put("expand", expandFields);
        }

        URI searchUri = buildURI("https://jira.intuit.com/rest/api/2/issue/"+jiraTicketId, queryParams);
        return searchUri;
	}

	public static URI myissuesUri(String user) throws URISyntaxException {
		Map<String, String> queryParams = new HashMap();
		queryParams.put("q", "is:open is:issue assignee:" +user +"archived:false");
        URI searchUri = buildURI(getBaseUri() + "search/issues", queryParams);
        return searchUri;
	}


}
