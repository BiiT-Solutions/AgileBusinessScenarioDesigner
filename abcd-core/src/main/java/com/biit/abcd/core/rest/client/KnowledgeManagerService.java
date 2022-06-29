package com.biit.abcd.core.rest.client;

import com.biit.abcd.configuration.AbcdConfigurationReader;
import com.biit.abcd.persistence.dao.IUserTokenDao;
import com.biit.abcd.persistence.entity.UserToken;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KnowledgeManagerService {

    @Autowired
    private IUserTokenDao userTokenDao;

    public CloseableHttpResponse login(String username, String password, Long userId) throws IOException {
        String url = AbcdConfigurationReader.getInstance().getKnowledgeManagerServiceLoginUrl();
        String jsonInputString = "{\"username\":" + "\"" + username + "\"" + ",\"password\":" + "\"" + password + "\"" + "}";

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(jsonInputString));
        httpPost.setHeader("Content-Type", "application/json");
        CloseableHttpResponse response = client.execute(httpPost);
        if (response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() <= 300) {
            UserToken userToken = new UserToken();
            userToken.setUserId(userId);
            userToken.setKnowledgeManagerAuthToken(response.getFirstHeader("Authorization").getValue());
            userTokenDao.merge(userToken);
        }
        return response;
    }

    public CloseableHttpResponse publishToKnowledgeManager(String metadata, String rules, String authToken, String email, String name, String version) throws IOException{
        String url = AbcdConfigurationReader.getInstance().getKnowledgeManagerServicePublishUrl();
        String jsonInputString = "{\"rules\":" + rules + ",\"email\":" + email +  ",\"metadata\":" + metadata + ",\"name\":" + name + ",\"version\":" + version + "}";
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new StringEntity(jsonInputString));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + authToken);
        CloseableHttpResponse response = client.execute(httpPost);
        return response;
    }
}
