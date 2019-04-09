package io.apicurio.kc.ext.social;

import org.keycloak.broker.oidc.OIDCIdentityProviderConfig;
import org.keycloak.models.KeycloakSession;
import org.keycloak.social.gitlab.GitLabIdentityProvider;

/**
 * A version of the GitLab identity provider that supports a local installation of GitLab.
 * @author eric.wittmann@gmail.com
 */
public class LocalGitLabIdentityProvider extends GitLabIdentityProvider {
    
    public static final String AUTH_FRAGMENT = "/oauth/authorize";
    public static final String TOKEN_FRAGMENT = "/oauth/token";
    public static final String USER_INFO_FRAGMENT = "/api/v4/user";

    /**
     * Constructor.
     * @param session
     * @param config
     */
    public LocalGitLabIdentityProvider(KeycloakSession session, OIDCIdentityProviderConfig config) {
        super(session, config);
        
        String baseUrl = config.getConfig().get("baseUrl");
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = System.getProperty("apicurio.hub.gitlab.url");
        }
        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            baseUrl = "https://gitlab.com";
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        System.out.println("(GitLab Local) Base URL: " + baseUrl);
        
        String authUrl = baseUrl + AUTH_FRAGMENT;
        String tokenUrl = baseUrl + TOKEN_FRAGMENT;
        String userInfoUrl = baseUrl + USER_INFO_FRAGMENT;
        
        config.setAuthorizationUrl(authUrl);
        config.setTokenUrl(tokenUrl);
        config.setUserInfoUrl(userInfoUrl);
    }

}
