package com.test.testmtlsserver1.constant.security;

import lombok.Getter;

@Getter
public enum AuthProviderEnum {

    KEYCLOAK("keycloak");
    private final String key;

    AuthProviderEnum(String key) {
        this.key = key;
    }


}
