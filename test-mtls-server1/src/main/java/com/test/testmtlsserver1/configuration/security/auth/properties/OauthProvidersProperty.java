package com.test.testmtlsserver1.configuration.security.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "oauth")
@Data
@Primary
public class OauthProvidersProperty {

    Map<String, OauthProviderProperty> providers;

    public String[] getProviderRoles() {
        Set<String> providerSet = new HashSet();
        providerSet.addAll(providers.values()
                .stream()
                .map(OauthProviderProperty::getRoles)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));

        return providerSet.toArray(String[]::new);
    }

}
