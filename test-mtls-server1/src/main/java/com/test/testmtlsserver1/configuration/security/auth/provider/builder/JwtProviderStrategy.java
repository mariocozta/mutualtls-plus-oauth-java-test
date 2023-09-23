package com.test.testmtlsserver1.configuration.security.auth.provider.builder;

import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class JwtProviderStrategy {

    private final Map<String, JwtAuthenticationProviderBuilder> jwtProviderMap;

    public JwtAuthenticationProvider getProvider(String provider) {
        if (jwtProviderMap.containsKey(provider)) {
            return jwtProviderMap.get(provider).getProvider();
        }
        return null;
    }

}
