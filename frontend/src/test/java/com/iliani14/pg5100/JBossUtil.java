package com.iliani14.pg5100;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by anitailieva on 03/10/2016.
 */
public class JBossUtil {

    private static ResteasyClient getClient() {
        // Setting digest credentials
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "admin");
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        HttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();
        ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(httpclient, true);

        // Creating HTTP client
        return new ResteasyClientBuilder().httpEngine(engine).build();
    }

    public static boolean isJBossUpAndRunning() {

        /*
            This is doing a REST HTTP call to query if Wildfly is up and running
         */

        try {
            WebTarget target = getClient().target("http://localhost:9990/management").queryParam("operation", "attribute").queryParam("name", "server-state");
            Response response = target.request(MediaType.APPLICATION_JSON).get();
            return response.getStatus() == Response.Status.OK.getStatusCode() && response.readEntity(String.class).contains("running");
        } catch (Exception e){
            return false;
        }
    }
}