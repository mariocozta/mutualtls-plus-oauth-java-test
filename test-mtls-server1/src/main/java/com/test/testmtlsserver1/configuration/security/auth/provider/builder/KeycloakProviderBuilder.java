package com.test.testmtlsserver1.configuration.security.auth.provider.builder;

import com.test.testmtlsserver1.configuration.security.auth.properties.OauthProvidersProperty;
import com.test.testmtlsserver1.constant.security.AuthProviderEnum;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;


@Component("keycloak")
public class KeycloakProviderBuilder extends AbstractAuthProviderBuilder {

    public KeycloakProviderBuilder(OauthProvidersProperty providersProperty) {
        super(providersProperty);
    }


    @Override
    protected JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setPrincipalClaimName("preferred_username");
        return converter;
    }

    protected String getProviderKey() {
        return AuthProviderEnum.KEYCLOAK.getKey();
    }
}
