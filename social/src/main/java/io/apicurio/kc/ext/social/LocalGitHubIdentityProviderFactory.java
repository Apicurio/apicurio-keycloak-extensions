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
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.social.github.GitHubIdentityProvider;

/**
 * Provider factory for the local github Enterprise identity provider.
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
        return new LocalGitHubIdentityProvider(session, new OAuth2IdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return "github";
    }

    @SuppressWarnings("unchecked") // safe b/c OAuth2IdentityProviderConfig extends IdentityProviderModel
    @Override
    public OAuth2IdentityProviderConfig createConfig() {
        return new OAuth2IdentityProviderConfig();
    }

}
