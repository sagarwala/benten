package com.intuit.benten.git.http;

import com.intuit.benten.common.http.HttpHelper;
import com.intuit.benten.git.properties.GitProperties;

import org.apache.http.HttpResponse;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.auth.BasicScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Divakar Ungatla
 * @version 1.0
 */
@Component
public class BentenGitHttpClient {

    @Autowired
    private HttpHelper httpHelper;

    @Autowired
    private GitProperties gitProperties;

    protected HttpResponse request(HttpRequestBase req) throws IOException {
        req.addHeader("Accept", "application/json");
        req.addHeader("Connection", "close");
        Credentials creds = new UsernamePasswordCredentials(gitProperties.getUsername(),gitProperties.getPassword());
        req.addHeader(BasicScheme.authenticate(creds, "utf-8", false));
        HttpResponse httpResponse = this.httpHelper.getClient().execute(req);
        return httpResponse;
    }
    
    protected HttpResponse jiraRequest(HttpRequestBase req) throws IOException {
        req.addHeader("Accept", "application/json");
        req.addHeader("Connection", "close");
        Credentials creds = new UsernamePasswordCredentials("svc_cetans_automation","Intuit123");
        req.addHeader(BasicScheme.authenticate(creds, "utf-8", false));
        HttpResponse httpResponse = this.httpHelper.getClient().execute(req);
        return httpResponse;
    }
}
