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

import org.jboss.logging.Logger;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.social.github.GitHubIdentityProvider;

/**
 * Provider factory for the local github identity provider.
 * 
 * @author eric.wittmann@gmail.com
 */
public class LocalGitHubIdentityProviderFactory
        extends AbstractIdentityProviderFactory<GitHubIdentityProvider>
        implements SocialIdentityProviderFactory<GitHubIdentityProvider> {

    protected static final Logger logger = Logger.getLogger(LocalGitHubIdentityProviderFactory.class);

    @Override
    public String getName() {
        return "GitHub Enterprise";
    }

    @Override
    public GitHubIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new LocalGitHubIdentityProvider(session, new OIDCIdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return "github";
    }

    @SuppressWarnings("unchecked") // safe b/c OAuth2IdentityProviderConfig does extend IdentityProviderModel
    @Override
    public <C extends IdentityProviderModel> C createConfig() {
        OAuth2IdentityProviderConfig config = new OIDCIdentityProviderConfig();

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
        logger.infov("GitHub Enterprise Base URL: {}",  baseUrl);

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
        logger.infov("GitHub Enterprise API URL: {}", apiUrl);

        String authUrl = baseUrl + LocalGitHubIdentityProvider.AUTH_FRAGMENT;
        String tokenUrl = baseUrl + LocalGitHubIdentityProvider.TOKEN_FRAGMENT;
        String profileUrl = apiUrl + LocalGitHubIdentityProvider.PROFILE_FRAGMENT;

        config.setAuthorizationUrl(authUrl);
        config.setTokenUrl(tokenUrl);
        config.setUserInfoUrl(profileUrl);

        return (C) config;
    }

}
