package com.test.testmtlsserver1.configuration.security.auth.provider.builder;


import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;

public interface JwtAuthenticationProviderBuilder {


    JwtAuthenticationProvider getProvider();

}
