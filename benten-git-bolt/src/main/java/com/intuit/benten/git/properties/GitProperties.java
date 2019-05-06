package com.intuit.benten.git.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Divakar Ungatla
 * @version 1.0
 */
@Component
@ConfigurationProperties("benten.github")
@PropertySource("classpath:benten.properties")
public class GitProperties {

    public static String baseurl;
    public static String username;
    public static String password;

    public GitProperties(){

    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
