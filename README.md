# apicurio-keycloak-extensions
Project that contains extensions to the Keycloak auth server.

## Social Connectors
This project contains two custom Keycloak identity providers, one for GitHub Enterprise and one for
GitLab (local).  As of version 6, Keycloak does not have support for locally installed versions of 
GitHub and GitLab beyond the standard **OpenID Connect v1.0** connector.  This connector does not
work well for GitLab and not at all for GitHub Enteprise.  The **social** module found in this
repository corrects this by providing GitHub and GitLab specific identity providers that can be
configured to point to local installations.

### GitLab Identity Provider
This package contains a variant of the Keycloak GitLab social provider that is capable of using a
local GitLab installation.  This solves the issues that appear when trying to use the Keycloak
OpenID Connect identity provider.

For more information about why this provider is necessary, see [this issue](https://github.com/Apicurio/apicurio-studio/issues/711).

The following are steps to using this provider:

1. Clone and build the custom Keycloak provider here:  https://github.com/Apicurio/apicurio-keycloak-extensions
2. Copy the resulting JAR file from `apicurio-keycloak-extensions/social/target/apicurio-keycloak-extensions-social-1.0.0-SNAPSHOT.jar` into your Keycloak installation's `standalone/deployments` directory
3. Before starting Keycloak, add the following system property:  `-Dapicurio.hub.gitlab.url=http://local-gitlab.example.org` where the value is obviously the real URL to your local GitLab install
4. When configuring Keycloak, you will now have two *GitLab* options when creating the Identity Provider.  Instead of choosing *GitLab* (or *OpenID Connect v1.0*) instead choose **GitLab (Local)**.
5. All other configuration is the same as documented for using the public GitLab service.

The result of doing this is that you will be using the custom GitLab ID provider implementation 
instead of the Keycloak baked-in one.  And the only difference between the custom one and the 
default is that custom provider checks for that System Property to determine the location of the 
various GitLab OpenID Connect endpoints.  So if you start Keycloak with the `apicurio.hub.gitlab.url` 
system property properly set, everything should work!


### GitHub Identity Provider
This package contains a variant of the Keycloak GitHub social provider that is capable of using a
local GitHub Enterprise installation.  This solves the issues that appear when trying to use the
Keycloak OpenID Connect identity provider.

For more information about why this provider is necessary, see [this issue](https://github.com/Apicurio/apicurio-studio/issues/831).

The following are steps to using this provider:

1. Clone and build the custom Keycloak provider(s) here: https://github.com/Apicurio/apicurio-keycloak-extensions
2. Copy the resulting JAR file from `apicurio-keycloak-extensions/social/target/apicurio-keycloak-extensions-social-6.0.0-SNAPSHOT.jar` into your Keycloak installation's `standalone/deployments` directory
3. Before starting Keycloak, add the following two system properties to it (either via command line or via standalone.xml):
```
-Dapicurio.hub.github.baseUrl=https://github.com
-Dapicurio.hub.github.apiUrl=https://api.github.com
```
> (Obviously change the above values to be your local GitHub Enterprise installation rather than the public URLs)
4. When configuring Keycloak, you will now have two *GitHub* options when creating the Identity Provider. Instead of choosing *GitHub* (or *OpenID Connect v1.0*) instead choose **GitHub (Local)**.
5. All other configuration is the same as documented for configuring public GitHub integration.

The result of doing this is that you will be using a custom GitHub Identity provider implementation 
instead of the Keycloak baked-in impl. And the only difference between mine and the default is that 
mine checks for those System Properties to determine the location of the various GitHub OpenID 
Connect endpoints. So if you start Keycloak with the `apicurio.hub.github.baseUrl` and 
`apicurio.hub.github.apiUrl` system properties properly set, everything should work!
