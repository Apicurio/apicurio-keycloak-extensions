package io.apicurio.kc.ext.social;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.social.gitlab.GitLabIdentityProvider;

/**
 * Provider factory for the local gitlab identity provider.
 * 
 * @author eric.wittmann@gmail.com
 */
public class LocalGitLabIdentityProviderFactory
        extends AbstractIdentityProviderFactory<GitLabIdentityProvider>
        implements SocialIdentityProviderFactory<GitLabIdentityProvider> {

    @Override
    public String getName() {
        return "GitLab (Local)";
    }

    @Override
    public GitLabIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new LocalGitLabIdentityProvider(session, new OIDCIdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return "gitlab";
    }

    @SuppressWarnings("unchecked") // safe b/c OIDCIdentityProviderConfig extends IdentityProviderModel
    @Override
    public OIDCIdentityProviderConfig createConfig() {
        return new OIDCIdentityProviderConfig();
    }

}
