package com.capitalone.dashboard.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.capitalone.dashboard.collector.GitWorkflowSettings;
import com.capitalone.dashboard.collector.WorkflowClient;
import com.capitalone.dashboard.misc.HygieiaException;


@RunWith(MockitoJUnitRunner.class)
public class DefaultWorkflowClientTest {
	
    @Mock private GitWorkflowSettings settings;
  
    @Mock   private  RestOperations restOperations;	

    @Mock	private WorkflowClient workflowClient;
    
    GitWorkflowRepo repo; 
    GitHubParsed gitHubParsed;
    String repoUrl;
    String apiUrl;
    
    @Before
    public void setUp() throws MalformedURLException, HygieiaException {
    	repo = new GitWorkflowRepo();
		repo.setRepoUrl("https://github.com/prash897/hygieia-workflow-github-collector.git");
		repo.setBranch("main");
		repo.setUserId("");
		repo.setPassword("");
		repo.setPersonalAccessToken("0095dfac9144f6f5b2f02eb8d82a185ef7230251");
		//repo.setPersonalAccessToken("");
		repo.getOptions().put(repo.REPO_URL, repo.getRepoUrl());
		
	    repoUrl = (String) repo.getOptions().get("url");
	    gitHubParsed = new GitHubParsed(repoUrl);
	    apiUrl = gitHubParsed.getApiUrl();
    }
    
	@Test
	public void test() throws MalformedURLException, HygieiaException {
		List<GitWorkflowRepo> workflows = workflowClient.getGitWorkflows(repo);
		assertTrue("The Workflows are", workflows.size()>0);
	}
	
	@Test
	public void makeRestCall_Test() {
		String queryUrl = apiUrl.concat("/actions/workflows" +
	        		"?branch=" + repo.getBranch());
        String decryptedPassword = repo.getPassword();
        String personalAccessToken = (String) repo.getPersonalAccessToken();
       
        ResponseEntity<String>  response = makeRestCall(queryUrl, repo.getUserId(), decryptedPassword, personalAccessToken);
        
        assertNotNull("The response from api", response);
	}
	

    public ResponseEntity<String> makeRestCall(String url, String userId,
                                                String password,String personalAccessToken) {
        // Basic Auth only.
        if (!"".equals(userId) && !"".equals(password)) {
            return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(userId, password)), String.class);
        } else if ((personalAccessToken!=null && !"".equals(personalAccessToken)) ) {
            return restOperations.exchange(url, HttpMethod.GET,new HttpEntity<>(createHeaders(personalAccessToken)),String.class);
        } else if (settings.getPersonalAccessToken() != null && !"".equals(settings.getPersonalAccessToken())){
            return restOperations.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(settings.getPersonalAccessToken())), String.class);
        }else {
            return restOperations.exchange(url, HttpMethod.GET, null, String.class);
        }
    }

    private HttpHeaders createHeaders(final String userId, final String password) {
        String auth = userId + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
        String authHeader = "Basic " + new String(encodedAuth);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

    private HttpHeaders createHeaders(final String token) {
        String authHeader = "token " + token;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);
        return headers;
    }

	
	

}
