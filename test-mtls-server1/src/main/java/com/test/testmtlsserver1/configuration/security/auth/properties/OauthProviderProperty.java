package com.test.testmtlsserver1.configuration.security.auth.properties;

import lombok.Data;

import java.util.List;

@Data
public class OauthProviderProperty {

    private String key;
    private String jwkSetUri;
    private String issuerUri;
    private List<String> roles;


}
