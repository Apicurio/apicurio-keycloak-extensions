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

    @Override
    public String getName() {
        return "GitHub (Local)";
    }

    @Override   
    public GitHubIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new LocalGitHubIdentityProvider(session, new OIDCIdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return "github";
    }

}
