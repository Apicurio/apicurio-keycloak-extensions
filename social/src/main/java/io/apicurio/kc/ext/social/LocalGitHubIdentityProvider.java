/*
 * Copyright 2019 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.kc.ext.social;

import java.util.Iterator;

import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;
import org.keycloak.social.github.GitHubIdentityProvider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * A version of the GitHub identity provider that supports a local installation of GitHub Enterprise.
 * @author eric.wittmann@gmail.com
 */
public class LocalGitHubIdentityProvider extends GitHubIdentityProvider {

    private static final Logger logger = Logger.getLogger(LocalGitHubIdentityProvider.class);

    public static final String AUTH_FRAGMENT = "/login/oauth/authorize";
    public static final String TOKEN_FRAGMENT = "/login/oauth/access_token";
    public static final String PROFILE_FRAGMENT = "/user";
    public static final String EMAIL_FRAGMENT = "/user/emails";

    private final String authUrl;
    private final String tokenUrl;
    private final String profileUrl;
    private final String emailUrl;

    /**
     * Constructor.
     * @param session
     * @param config
     */
    public LocalGitHubIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
        super(session, config);

        // The Base URL: https://github.com
        String baseUrl = config.getConfig().get("baseUrl");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = System.getProperty("apicurio.hub.github.baseUrl");
        }
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = "https://github.com";
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        logger.infov("GitHub Base URL: {0}", baseUrl);

        // The Base URL: https://api.github.com
        String apiUrl = config.getConfig().get("apiUrl");
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            apiUrl = System.getProperty("apicurio.hub.github.apiUrl");
        }
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            apiUrl = "https://api.github.com";
        }
        if (apiUrl.endsWith("/")) {
            apiUrl = apiUrl.substring(0, apiUrl.length() - 1);
        }
        logger.infov("GitHub API URL: {0}", apiUrl);

        authUrl = baseUrl + AUTH_FRAGMENT;
        tokenUrl = baseUrl + TOKEN_FRAGMENT;
        profileUrl = apiUrl + PROFILE_FRAGMENT;
        emailUrl = apiUrl + EMAIL_FRAGMENT;

        config.setAuthorizationUrl(authUrl);
        config.setTokenUrl(tokenUrl);
        config.setUserInfoUrl(profileUrl);
    }
    
    /**
     * @see org.keycloak.social.github.GitHubIdentityProvider#getProfileEndpointForValidation(org.keycloak.events.EventBuilder)
     */
    @Override
    protected String getProfileEndpointForValidation(EventBuilder event) {
        return profileUrl;
    }
    
    /**
     * @see org.keycloak.social.github.GitHubIdentityProvider#doGetFederatedIdentity(java.lang.String)
     */
    @Override
    protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
        try {
            JsonNode profile = SimpleHttp.doGet(profileUrl, session).header("Authorization", "Bearer " + accessToken).asJson();

            BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);

            if (user.getEmail() == null) {
                user.setEmail(searchEmail(accessToken));
            }

            return user;
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user profile from github.", e);
        }
    }

    private String searchEmail(String accessToken) {
        try {
            ArrayNode emails = (ArrayNode) SimpleHttp.doGet(emailUrl, session).header("Authorization", "Bearer " + accessToken).asJson();

            Iterator<JsonNode> loop = emails.elements();
            while (loop.hasNext()) {
                JsonNode mail = loop.next();
                if (mail.get("primary").asBoolean()) {
                    return getJsonProperty(mail, "email");
                }
            }
        } catch (Exception e) {
            throw new IdentityBrokerException("Could not obtain user email from github.", e);
        }
        throw new IdentityBrokerException("Primary email from github is not found.");
    }


}
