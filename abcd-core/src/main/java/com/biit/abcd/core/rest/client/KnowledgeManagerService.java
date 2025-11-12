package com.biit.abcd.core.rest.client;

/*-
 * #%L
 * Agile Business sCenario Designer Tool (Core)
 * %%
 * Copyright (C) 2022 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

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
